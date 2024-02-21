package net.xnzn.photo.config;

import lombok.Getter;
import net.xnzn.photo.util.RetCodeEnum;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
@Getter
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = -3303518302920463234L;

    private final RetCodeEnum status;

    public ServiceException(RetCodeEnum status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(RetCodeEnum status) {
        this(status, status.getDesc());
    }
}
