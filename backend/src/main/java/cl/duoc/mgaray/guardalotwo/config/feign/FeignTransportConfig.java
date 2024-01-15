package cl.duoc.mgaray.guardalotwo.config.feign;

import cl.duoc.mgaray.guardalotwo.apiclients.transport.TransportErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTransportConfig {
    @Bean
    public ErrorDecoder feignTransportErrorDecoder() {
        return new TransportErrorDecoder();
    }

    @Bean
    Logger.Level feignTransportLoggerLevel() {
        return Logger.Level.FULL;
    }
}
