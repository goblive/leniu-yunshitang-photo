package net.xnzn.photo.dto;

import lombok.Data;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
@Data
public class OpenPhotoDTO {
    //人员id
    private String custId;
    //人员名称
    private String username;
    //公司id
    private String companyId;
    //照片base64
    private String base64Str;
    //签名
    private String sign;


}
