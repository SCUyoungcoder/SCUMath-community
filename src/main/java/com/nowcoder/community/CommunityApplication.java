package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication/*注解表示是一个配置文件*/
/*启动tomcat，创建spring容器*/
public class CommunityApplication {
    /*管理bean的生命周期，管理bean的初始化。下面方法在构造器调用完之后执行*/
    @PostConstruct
    public void init(){
        //解决netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {

        SpringApplication.run(CommunityApplication.class, args);
    }
}
