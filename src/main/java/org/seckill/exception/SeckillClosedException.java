package org.seckill.exception;

/**
 * 秒杀关闭异常
 */
public class SeckillClosedException extends SeckillException{

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillClosedException(String message) {
        super(message);
    }
}
