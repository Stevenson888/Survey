package com.xwl;

import com.xwl.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 项目启动类
 */
@SpringBootApplication
public class AdminBootApplication {
    //解决druid 日志报错：discard long time none received connection:xxx
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }

    public static void main(String[] args) {
//        SpringApplication.run(AdminBootApplication.class,args);

        ApplicationContext context = SpringApplication.run(AdminBootApplication.class,args);
        SpringUtil.setApplicationContext(context);
    }

}
