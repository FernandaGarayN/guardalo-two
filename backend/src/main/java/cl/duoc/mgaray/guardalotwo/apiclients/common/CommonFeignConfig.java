package cl.duoc.mgaray.guardalotwo.apiclients.common;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonFeignConfig {
    @Bean
    public ErrorDecoder commonFeignErrorDecoder() {
        return new CommonErrorDecoder();
    }

    @Bean
    Logger.Level commonFeignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
