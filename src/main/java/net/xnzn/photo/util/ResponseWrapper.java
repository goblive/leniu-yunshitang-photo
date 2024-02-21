package net.xnzn.photo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResponseWrapper<T> implements Serializable {
    /**
     * 返回码，10000-成功，40004-失败
     */
    protected Integer code = 10000;
    /**
     * 返回信息
     */
    protected String msg = "成功";
    /**
     * 返回数据
     */
    protected T data;

    public ResponseWrapper(T data) {
        this.data = data;
    }

    public static ResponseWrapper<Object> fails(Integer code, String errorMsg) {
        ResponseWrapper<Object> apiResult = new ResponseWrapper<>();
        apiResult.setCode(code == null ? 40004 : code);
        apiResult.setMsg(errorMsg);
        return apiResult;
    }

}
