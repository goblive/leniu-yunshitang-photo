package net.xnzn.photo.controller;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xnzn.photo.config.ServiceException;
import net.xnzn.photo.dto.DevicePhotoDTO;
import net.xnzn.photo.model.CustPhoto;
import net.xnzn.photo.service.CustPhotoService;
import net.xnzn.photo.util.LeResponse;
import net.xnzn.photo.util.RetCodeEnum;
import net.xnzn.photo.vo.DeviceCustPhotoVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * H5人脸识别,设备拉取特征值
 *
 * author: lzg
 * date: 2024-02-20
 */

@RestController
@CrossOrigin
@RequestMapping("custPhoto")
public class CustPhotoController {
    /**
     * 服务对象
     */
    @Resource
    private CustPhotoService custPhotoService;

    /**
     * 注册人脸
     *
     * author: lzg
     * date: 2024-02-20
     */
    @PostMapping("/registerFace")
    public LeResponse<String> registerFace(MultipartFile file, String custId, String username, String companyId) {
        InputStream imageStream;
        try {
            imageStream = file.getInputStream();
            //判断不能上传空文件
            if (imageStream.available() == 0) {
                return LeResponse.fail(RetCodeEnum.FAIL, "请选择上传图片！");
            }
        } catch (IOException e) {
            return LeResponse.fail(RetCodeEnum.FAIL, null, "图片数据流提取失败！");
        }
        if (file.getSize() > 0.5 * 1024 * 1024) {
            throw new ServiceException(RetCodeEnum.FAIL, "照片大小不能超过500kb");
        }
        String imageBse64 = Base64.encode(imageStream);

        custPhotoService.phptoRegister(imageBse64, custId, username, companyId);
        return LeResponse.succ("人脸录入成功！");
    }

    /**
     * 根据id查照片base64
     *
     * author: lzg
     * date: 2024-02-20
     */
    @PostMapping("/queryCustInfoByCustId")
    public LeResponse<String> queryCustInfoByCustId(@RequestBody DevicePhotoDTO devicePhotoDTO) {

        //必传校验
        checkParams(devicePhotoDTO);
        CustPhoto custPhoto = custPhotoService.getOne(Wrappers.lambdaQuery(CustPhoto.class)
                .eq(CustPhoto::getCustId, devicePhotoDTO.getCustId())
                .eq(CustPhoto::getCompanyId, devicePhotoDTO.getCompanyId()));
        //如果没有照片返回空字符串
        String base64 = Optional.ofNullable(custPhoto).map(CustPhoto::getBase64).orElse("");
        return LeResponse.succ(base64);
    }
    /**
     * 删除人脸
     *
     * author: lzg
     * date: 2024-02-20
     */
    @PostMapping("/deleteByCustId")
    public LeResponse<String> deleteByCustId(@RequestBody DevicePhotoDTO devicePhotoDTO) {
        //必传校验
        checkParams(devicePhotoDTO);
        custPhotoService.deletePhoto(devicePhotoDTO.getCustId(), devicePhotoDTO.getCompanyId());
        return LeResponse.succ("人脸删除成功！");
    }

    /**
     * 设备拉取特征值
     *
     * author: lzg
     * date: 2024-02-20
     */

    @PostMapping(value = "/featureList")
    public LeResponse<List<DeviceCustPhotoVO>> featureList(@RequestBody DevicePhotoDTO devicePhotoDTO) {

        int pagesize = ObjectUtil.isNotEmpty(devicePhotoDTO.getPagesize()) ? devicePhotoDTO.getPagesize() : 500;

        Page<CustPhoto> page = new Page<>(1, pagesize);

        LambdaQueryWrapper<CustPhoto> queryWrapper = Wrappers.lambdaQuery(CustPhoto.class)
                .eq(CustPhoto::getCompanyId, devicePhotoDTO.getCompanyId())
                .gt(CustPhoto::getUpdateId, devicePhotoDTO.getUpdateId())
                .orderByAsc(CustPhoto::getUpdateId);
        Page<CustPhoto> photoPage = custPhotoService.page(page, queryWrapper);
        List<DeviceCustPhotoVO> collect = photoPage.getRecords().stream().map(e -> {
            DeviceCustPhotoVO photoVO = new DeviceCustPhotoVO();
            photoVO.setCustId(e.getCustId());
            photoVO.setFeatures(e.getFeatures());
            photoVO.setUserName(e.getUserName());
            photoVO.setCompanyId(e.getCompanyId());
            photoVO.setUpdateId(e.getUpdateId());
            photoVO.setUptime(e.getUptime());
            return photoVO;
        }).collect(Collectors.toList());


        return LeResponse.succ(collect);

    }







    private void checkParams(DevicePhotoDTO devicePhotoDTO) {
        if (ObjectUtil.isEmpty(devicePhotoDTO.getCustId())) {
            throw new ServiceException(RetCodeEnum.FAIL, "custId不能为空");
        }
        if (ObjectUtil.isEmpty(devicePhotoDTO.getCompanyId())) {
            throw new ServiceException(RetCodeEnum.FAIL, "companyId不能为空");
        }
    }

}

