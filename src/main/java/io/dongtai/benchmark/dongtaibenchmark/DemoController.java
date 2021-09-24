package io.dongtai.benchmark.dongtaibenchmark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
public class DemoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("test")
    @ResponseBody
    public String testDemo(){
        String sql = "select name from test where test.a = 1";
        List<String> names = this.jdbcTemplate.queryForList(sql,String.class);
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : names) {
            redisTemplate.boundValueOps(name).set(name);
            stringBuilder.append(name);
            redisTemplate.boundValueOps(name).get();
        }
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return stringBuilder.toString();
    }
}
