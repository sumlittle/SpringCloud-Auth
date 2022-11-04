package com.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
//排除此类的autoconfig
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class GateWayServer {
    public static void main(String[] args) {
        SpringApplication.run(GateWayServer.class, args);
    }
}