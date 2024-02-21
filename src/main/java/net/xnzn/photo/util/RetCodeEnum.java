/*
 * Copyright(C),2020-2028,煜城骁特
 * FileName:net.xnzn.leniu.yunshitang.enums.RetCodeEnum
 * Author:shihao.li
 * History:
 *     <author> <time> <version> <desc>
 *     作者       修改时间        版本      描述
 */
package net.xnzn.photo.util;

import lombok.Getter;

/**
 *
 *
 * author: lzg
 * date: 2024-02-20
 */
@Getter
public enum RetCodeEnum {
    /**
     * 请求成功，默认返回码和描述
     */
    SUCC(10000, "成功"),

    /**
     * 请求失败，默认返回码和描述
     */
    FAIL(40004, "失败"),

    PAY_PERSONAL_NO_EXIT(50002, "人员不存在"),
    PAY_CARD_NO_EXIT(50003, "卡号不正确"),
    PAY_CARD_STATE_ABNORMAL(50004, "卡片异常"),
    PAY_ACC_STATE_ABNORMAL(50005, "账户异常"),

    /**
     * 人员模块错误返回码
     **/
    CUST_PERSONAL_NO_EXIT(52001, "人员不存在"),

    /**
     * 人脸注册相关 59000-59010
     */
    FACE_REGISTER_FAIL(59000, "人脸照片上传注册失败!");

    private final Integer key;
    private final String desc;

    RetCodeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
