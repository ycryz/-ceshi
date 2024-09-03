package cn.jx.ceshi.ceshi;

import cn.jx.ceshi.ceshi.common.RedisCommon;
import cn.jx.ceshi.ceshi.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class CeshiApplicationTests {
    @Autowired
    private RedisService redisService;
    @Test
    void contextLoads() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTime = formatter.format(date);
        System.out.println(formattedTime);
    }

    @Test
    void test(){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("123","456");

        redisService.setCacheMap("ceshi:123",objectObjectHashMap);
        System.out.println(redisService.getCacheMap("ceshi:123"));
    }

}
