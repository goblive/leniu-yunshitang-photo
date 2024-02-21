package net.xnzn.photo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
@Data
public class DeviceCustPhotoVO {
    /**
     * 人员id
     */

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
     * 更新时间
     */
    private LocalDateTime uptime;
}
