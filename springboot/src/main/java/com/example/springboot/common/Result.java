package com.example.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String code; // 返回状态码
    private String msg; // 返回消息
    private Object data; // 返回数据

    /**
     * 构造成功的Result对象，无返回数据
     *
     * @return Result对象
     */
    @NotNull
    @Contract(" -> new")
    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    /**
     * 构造成功的Result对象，带返回数据
     *
     * @param data 返回数据
     * @return Result对象
     */
    @NotNull
    @Contract("_ -> new")
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    /**
     * 构造失败的Result对象，带错误码和错误消息
     *
     * @param code 错误码
     * @param msg  错误消息
     * @return Result对象
     */
    @NotNull
    @Contract("_, _ -> new")
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    /**
     * 构造系统错误的Result对象
     *
     * @return Result对象
     */
    @NotNull
    @Contract(" -> new")
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }
}
