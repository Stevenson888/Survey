package com.xwl.exception;

import com.xwl.controller.UserController;
import com.xwl.utils.ResultUtils;
import com.xwl.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出的的是ServiceException，则调用该方法
     * @param se 业务异常
     * @return Result
     */
    //@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)   //er.js:87 Uncaught (in promise) Error: Request failed with status code 418
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResultVo handler(ServiceException se){
        // ResultUtils: public static ResultVo error(String msg,int code,Object data){
        return ResultUtils.error( se.getMessage(),  Integer.valueOf(se.getCode()), null);
    }

//    /**
//     * ShiroException:捕获登录时，shiro抛出的异常，比如没有权限，用户登录异常
//     */
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = ShiroException.class)
//    public Result handle401(ShiroException e) {
//        log.error("登录异常：----------------{}", e);
//        return Result.fail(401, e.getMessage(), null);
//    }

    /**
     * IllegalArgumentException:处理Assert的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultVo handler(IllegalArgumentException e) throws IOException {
        log.error("Assert异常:-------------->{}",e);
        log.error("Assert异常:-------------->{}",e.getMessage());
        e.printStackTrace();
        return ResultUtils.error(e.getMessage());
    }

    /**
     * MethodArgumentNotValidException: 此方法为处理实体校验异常
     * 处理Pojo列上的@Validated没通过的情况
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultVo handler(MethodArgumentNotValidException e) throws IOException {
        log.error("运行时异常-实体校验异常:-------------->",e);
        log.error("运行时异常:-------------->",e.getMessage());
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return ResultUtils.error(objectError.getDefaultMessage());
    }

    /**
     * RuntimeException: 捕获其他异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public ResultVo handler(RuntimeException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        log.error("运行时异常:-------------->",e.getMessage());
        e.printStackTrace();
        return ResultUtils.error(e.getMessage());
    }



}
