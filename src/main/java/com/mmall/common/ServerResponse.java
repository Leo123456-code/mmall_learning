package com.mmall.common;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * ClassName: ServerResponse
 * Description: 统一页面返回状态
 * Author: Leo
 * Date: 2020/3/8-2:28
 * email 1437665365@qq.com
 */
@Data
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = 4095343419963698811L;

    private Integer status;

    private String msg;

    private T data;

    private ServerResponse(Integer status) {
        this.status = status;
    }

    private ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess() {
        /**
         * @Description 成功返回的状态
           @Author Leo
          *@Date 2:52 2020/3/8
         * @Param []
         * @return boolean
         */
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    //失败
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String msg) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }


}
