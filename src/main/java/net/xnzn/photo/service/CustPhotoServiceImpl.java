package net.xnzn.photo.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.xnzn.photo.config.ServiceException;
import net.xnzn.photo.mapper.CustPhotoMapper;
import net.xnzn.photo.model.CustPhoto;
import net.xnzn.photo.util.RetCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
/**
 * 人员生物识别特征表(CustPhoto)表服务实现类
 *
 * @author lzg
 * @since 2024-02-19 18:20:28
 */

/**
 * 人员生物识别特征表(CustPhoto)表服务实现类
 *
 * @author makejava
 * @since 2024-02-19 18:20:28
 */
@Slf4j
@Service("custPhotoService")
public class CustPhotoServiceImpl extends ServiceImpl<CustPhotoMapper, CustPhoto> implements CustPhotoService {

    @Value("${open.faceApiUrl}")
    private String faceApiUrl;

    @Override
    public void phptoRegister(String imageBse64, String custId,String username,String companyId) {
        //获取特征值
        String feature = generate3288FaceFeature(imageBse64);

        //查人
        CustPhoto photo = getOne(Wrappers.lambdaQuery(CustPhoto.class)
                .eq(CustPhoto::getCustId, custId)
                .eq(CustPhoto::getCompanyId, companyId));
        //保存或更新特征值
        CustPhoto custPhoto = new CustPhoto();
        custPhoto.setCustId(custId);
        custPhoto.setUserName(username);
        custPhoto.setCompanyId(companyId);
        custPhoto.setFeatures(feature);
        custPhoto.setBase64(imageBse64);
        custPhoto.setUpdateId(IdUtil.getSnowflake(1, 1).nextId());
        if (photo != null) {
           updateById(custPhoto);
        } else {
            save(custPhoto);
        }


    }

    //删除特征值就是置空
    @Override
    public void deletePhoto(String custId, String companyId) {

        this.update(Wrappers.lambdaUpdate(CustPhoto.class)
                .set(CustPhoto::getFeatures, "")
                .set(CustPhoto::getBase64, "")
                .set(CustPhoto::getUpdateId, IdUtil.getSnowflake(1, 1).nextId())
                .eq(CustPhoto::getCustId, custId)
                .eq(CustPhoto::getCompanyId, companyId));
    }

    public  String generate3288FaceFeature(String imageBase64) {
        try {
//            String apiUrl = "http://8.142.137.85:7999/face/new2.0/feature";
            log.info("[旷视多模型]请求地址：{}", faceApiUrl);
            JSONObject params = new JSONObject();
            params.put("listFeature", Collections.singletonList("3288"));
            params.put("base64", imageBase64);

            String responseStr = HttpUtil.createPost(faceApiUrl)
                    .setConnectionTimeout(12 * 1000)
                    .body(params.toJSONString()).execute().body();
            log.info("[旷视多模型]响应结果：{}", responseStr);
            JSONObject resObj = JSON.parseObject(responseStr);
            Integer code = resObj.getInteger("code");
            if (10000 == code) {
                JSONObject jsonObject = resObj.getJSONObject("data");
                return jsonObject.getString("feature3288");
            } else {
                throw new ServiceException(RetCodeEnum.FAIL, resObj.getString("msg"));
            }
        } catch (ServiceException e) {
            throw new ServiceException(RetCodeEnum.FAIL, e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(RetCodeEnum.FAIL, "旷视多模型请求异常,请稍后重试");
        }

    }


}

