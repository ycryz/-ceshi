package cn.jx.ceshi.ceshi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@MapperScan({"cn.jx.ceshi.ceshi.mapper","cn.jx.ceshi.ceshi.common"})
@SpringBootApplication
@EnableCaching
public class CeshiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CeshiApplication.class, args);
    }

}
