package com.xwl.service.impl;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xwl.annotation.AutoLog;
import com.xwl.entity.User;
import com.xwl.utils.IpUtils;
import com.xwl.utils.SessionUtils;
import com.xwl.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {

    /**
     * 通过@Pointcut指定切入点 ，这里指定的切入点为Log注解类型，也就是
     被@Log注解修饰的方法，进入该切入点
     */
    @Pointcut(value = "@annotation(com.xwl.annotation.AutoLog)")
    private void pointcut() {

    }


//    @Around(value = "@annotation(autoLog)")
    @Around(value = "pointcut() && @annotation(autoLog)")
    public Object autoLog(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        // 方法的开始时间
        long beginTime = SystemClock.now();
        //执行方法
        Object result = joinPoint.proceed();
        // 方法的执行时长(毫秒)
        long time = SystemClock.now() - beginTime;

        // 操作
        String autoLogName = autoLog.value();

        // 请求
        HttpServletRequest request = getRequest();
        // 请求的url
        String url = request.getRequestURL().toString();
//        // 请求的方法名
//        String methodName = request.getMethod();

        if (url.contains("/file/download")) {
            return result;
        }

        // 请求方法
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        // 请求的方法名
        Method requestMethod = methodSignature.getMethod();
        // 请求方法的类名+方法名
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 请求方法的参数
        String requestParams = getRequestParams(joinPoint, request);

//        // 请求参数
//        String params = "";
//        Object[] args = joinPoint.getArgs();
//        if (args.length > 0) {
//            Object arg0 = args[0];
//            if (arg0 instanceof MultipartFile) {
//                params = "文件流";
//            } else if (arg0 instanceof String || arg0 instanceof Long || arg0 instanceof Integer) {
//                params = arg0.toString();
//            } else {
//                params = JSONUtil.toJsonStr(args[0]);
//            }
//        }
//        if (params.length() > 1000) {
//            params = "数据过大";
//        }

        // 请求的返回数据
        String returnValue = JSONUtil.toJsonStr(result);
        if (returnValue.length() > 1000) {
            returnValue = "数据过大，返回状态码：" + JSONUtil.parseObj(returnValue).getStr("code");
        }

         // 用户: 用户名
        String username = "";
        String realName = "";
        Long userId = new Long(0);
        User user = TokenUtils.getCurrentUser();
        if (user != null) {
            userId = user.getUserId();
            realName = user.getRealName();
            username = user.getUsername();
        }

        // 用户: ip 和 地址
        Dict ipAndCity = IpUtils.getIPAndCity();
        String ip = ipAndCity.getStr("ip");
        String city = ipAndCity.getStr("city");


        // 保存到数据库
//        Log log = Log.builder().name(name).params(params).output(returnValue).url(url).ip(ip)
//                .address(city).duration((int) time).username(username).build();
//        logService.save(log);

        log.info(
                " AutoLogName:" + autoLogName
                + "; UserId:" + userId
                + "; RealName:" + realName
                + "; Username:" + username
                + "; URL:" + url
                + "; RequestMethod:" + requestMethod
                + "; MethodName:" + methodName
//                + "; MethodParams:" + params
                + "; RequestParams:" + requestParams
                + "; MethodReturnValue:" + returnValue
                + "; IP:" + ip
                + "; City:" + city
                + "; Duration:" + time+ "s"
                + ";"
        );
//                +"\n"+ method.getDeclaringClass().getName()+"."+ method.getName()+":"+ Arrays.toString(args)+"\n"+"result:"+result);

        return result;
    }

    /**
     *  异常通知
     *
     * 在目标方法出现异常时会执行的代码.
     * 可以访问到异常对象; 且可以指定在出现特定异常时在执行通知代码
     *
     * @param joinPoint
     * @param e
     */
//    @AfterThrowing(value = "autoLog", throwing = "e")
    @AfterThrowing(value = "pointcut() && @annotation(com.xwl.annotation.AutoLog)", throwing = "e")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception e){
        System.out.println("异常通知, 出现异常 ：" + e);
    }

    /**
     * 获取请求
     *
     * @return
     */
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public String getRequestParams(ProceedingJoinPoint point, HttpServletRequest request) {
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        Method method = methodSignature.getMethod();
        String queryString = null;
        String requestMethod = request.getMethod();
        if (requestMethod.equals(HttpMethod.POST.name())) {
            //参数值
            Object[] args = ArrayUtils.toArray(point.getArgs());
            queryString = JSON.toJSONString(args[0], SerializerFeature.WriteMapNullValue);
        }
        else {
            //参数名
            Parameter[] parameters = method.getParameters();
            //参数值
            Object[] args = ArrayUtils.toArray(point.getArgs());
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                map.put(parameters[i].getName(), args[i]);
            }
            queryString = JSON.toJSONString(map);
        }
        return queryString;
    }


}
