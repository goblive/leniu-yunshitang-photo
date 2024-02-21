package net.xnzn.photo.config;

import lombok.extern.slf4j.Slf4j;
import net.xnzn.photo.util.LeResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    /**
     * 处理ServiceException
     * @param serviceException ServiceException
     * @param request 请求参数
     * @return 接口响应
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public LeResponse<Void> handleServiceException(ServiceException serviceException, HttpServletRequest request) {
        log.warn("request {} throw ServiceException \n", request, serviceException);
        return LeResponse.fail(serviceException.getStatus(), serviceException.getMessage());
    }

    /**
     * 其他异常拦截
     * @param ex 异常
     * @param request 请求参数
     * @return 接口响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public LeResponse<Void> handleException(Exception ex, HttpServletRequest request) {
        log.error("request {} throw unExpectException \n", request, ex);
        return LeResponse.fail();
    }
}
