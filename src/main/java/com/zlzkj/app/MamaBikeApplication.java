package com.zlzkj.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"beans.xml"})
@MapperScan(basePackages = {"com.zlzkj.app.mapper"})
public class MamaBikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MamaBikeApplication.class, args);
	}
}
