package net.xnzn.photo.dto;

import lombok.Data;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
@Data
public class DevicePhotoDTO {
    //人员id
    private String custId;
    //增量id
    private Long updateId;
    //单次查询数量
    private Integer pagesize;
    //公司id
    private String companyId;
}
