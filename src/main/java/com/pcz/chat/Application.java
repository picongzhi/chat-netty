package com.pcz.chat;

import com.pcz.chat.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author picongzhi
 */
@SpringBootApplication(scanBasePackages = {"com.pcz"})
@MapperScan(basePackages = "com.pcz.chat.mapper")
@EnableTransactionManagement
public class Application {
    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
