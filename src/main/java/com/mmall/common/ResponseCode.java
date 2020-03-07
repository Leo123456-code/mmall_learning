package com.mmall.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: ResponseCode
 * Description: 状态枚举
 * Author: Leo
 * Date: 2020/3/8-2:42
 * email 1437665365@qq.com
 */
@AllArgsConstructor
@Getter
public enum  ResponseCode {
    SUCCESS(0,"成功"),
    ERROR(1,"失败"),
    NEED_LOGIN(10,"需要登录"),
    ILLEGAL_ARGUMENT(2,"参数错误"),
    ;

    private final Integer code;

    private final String desc;
}
