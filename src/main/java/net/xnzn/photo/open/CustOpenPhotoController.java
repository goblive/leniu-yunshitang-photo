package net.xnzn.photo.open;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xnzn.photo.config.ServiceException;
import net.xnzn.photo.dto.OpenPhotoDTO;
import net.xnzn.photo.service.CustPhotoService;
import net.xnzn.photo.util.ApiSignUtil;
import net.xnzn.photo.util.LeResponse;
import net.xnzn.photo.util.RetCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.InputStream;


/**
 * 提供open接口进行人脸录入和删除
 * <p>
 * author: lzg
 * date: 2024-02-20
 */
@RestController
@RequestMapping("open")
public class CustOpenPhotoController {

    @Value("${open.secretKey}")
    private String secretKey;

    /**
     * 服务对象
     */
    @Resource
    private CustPhotoService custPhotoService;

    /**
     * 注册人脸
     * <p>
     * author: lzg
     * date: 2024-02-20
     */
    @PostMapping("/openRegisterFace")
    public LeResponse<String> openRegisterFace(@RequestBody OpenPhotoDTO openPhotoDTO) {
        //必传校验
//        checkParams(openPhotoDTO);

        //读取照片
        String imageUrl = openPhotoDTO.getImageUrl();
        if (ObjectUtil.isNotEmpty(imageUrl)) {
            //设置超时时间

            InputStream inputStream = null;
            try {
                inputStream = HttpUtil.createGet(imageUrl).timeout(10 * 1000).execute().bodyStream();
            } catch (Exception e) {
                throw new ServiceException(RetCodeEnum.FAIL, "图片地址连接超时！");
            }
            String encode = Base64.encode(inputStream);
            openPhotoDTO.setBase64Str(encode);
        }
        byte[] decodedBytes = Base64.decode(openPhotoDTO.getBase64Str());
        // 获取字节数组的长度
        long fileSizeInBytes = decodedBytes.length;

        if (fileSizeInBytes > 0.5 * 1024 * 1024) {
            throw new ServiceException(RetCodeEnum.FAIL, "照片大小不能超过500KB！");
        }

        custPhotoService.phptoRegister(openPhotoDTO.getBase64Str(), openPhotoDTO.getCustId(), openPhotoDTO.getUsername(), openPhotoDTO.getCompanyId());
        return LeResponse.succ("人脸录入成功！");
    }


    @PostMapping("/openDeleteByCustId")
    public LeResponse<String> openDeleteByCustId(@RequestBody OpenPhotoDTO openPhotoDTO) {
        //必传校验
        checkParams(openPhotoDTO);
        custPhotoService.deletePhoto(openPhotoDTO.getCustId(), openPhotoDTO.getCompanyId());
        return LeResponse.succ("人脸删除成功！");
    }

    public void checkParams(OpenPhotoDTO devicePhotoDTO) {
        if (ObjectUtil.isEmpty(devicePhotoDTO.getCustId())) {
            throw new ServiceException(RetCodeEnum.FAIL, "custId不能为空");
        }
        if (ObjectUtil.isEmpty(devicePhotoDTO.getCompanyId())) {
            throw new ServiceException(RetCodeEnum.FAIL, "companyId不能为空");
        }
        if (ObjectUtil.isAllEmpty(devicePhotoDTO.getBase64Str(), devicePhotoDTO.getImageUrl())) {
            throw new ServiceException(RetCodeEnum.FAIL, "照片base64或url必填二选一");
        }
        if (ObjectUtil.isEmpty(devicePhotoDTO.getSign())) {
            throw new ServiceException(RetCodeEnum.FAIL, "sign不能为空");
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(devicePhotoDTO);
        boolean b = ApiSignUtil.verifySignature(jsonObject, secretKey, devicePhotoDTO.getSign());
        if (!b) {
            throw new ServiceException(RetCodeEnum.FAIL, "验签失败");
        }
    }

}
