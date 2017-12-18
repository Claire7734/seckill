package org.seckill.dto;

/**
 * 封装json结果
 * @param <T>
 */
//所有ajax请求返回类型
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;//对失败情况的解释 e.getMessage()

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
