package net.xnzn.photo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人员生物识别特征表(CustPhoto)实体类
 *
 * @author lzg
 * @date 2024-02-20 14:14:49
 */
@Data
@TableName("cust_photo")
public class CustPhoto implements Serializable {
    private static final long serialVersionUID = -84443415617476968L;

    /**
     * 人员id
     */
    @TableId
    private String custId;
    /**
     * 增量id
     */
    private Long updateId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 特征码
     */
    private String features;
    /**
     * 照片base64
     */
    private String base64;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 创建人
     */
    private String crby;
    /**
     * 创建时间
     */
    private LocalDateTime crtime;
    /**
     * 更新人
     */
    private String upby;
    /**
     * 更新时间
     */
    private LocalDateTime uptime;

}

