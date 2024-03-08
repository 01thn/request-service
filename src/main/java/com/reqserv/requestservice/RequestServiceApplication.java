package com.reqserv.requestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("com.reqserv.requestservice.dto.mapper")
public class RequestServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RequestServiceApplication.class, args);
  }

}
