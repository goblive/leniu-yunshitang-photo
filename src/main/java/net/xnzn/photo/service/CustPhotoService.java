package net.xnzn.photo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.xnzn.photo.model.CustPhoto;

import java.io.InputStream;
/**
 * 人员生物识别特征表(CustPhoto)表服务接口
 *
 * @author lzg
 * @since 2024-02-19 18:20:26
 */
/**
 * 人员生物识别特征表(CustPhoto)表服务接口
 *
 * @author makejava
 * @since 2024-02-19 18:20:26
 */
public interface CustPhotoService extends IService<CustPhoto> {

     void phptoRegister(String imageBse64, String custId,String username,String companyId);

     void deletePhoto(String custId, String companyId);
}

