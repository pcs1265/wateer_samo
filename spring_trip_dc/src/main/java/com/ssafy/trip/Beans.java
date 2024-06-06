package com.ssafy.trip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ssafy.trip.*.model.mapper")
public class Beans {
}
