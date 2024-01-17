package cl.duoc.mgaray.guardalotwo.apiclients.transport;

import cl.duoc.mgaray.guardalotwo.config.feign.FeignTransportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "transport-client",
        url = "http://172.20.10.4:8080",
        configuration = FeignTransportConfig.class)
public interface TransportClient {
    @PostMapping("/solicitudes")
    TransportResponsePostRequest postRequest(@RequestBody TransportRequestPostRequest request);

    @GetMapping("/solicitudes/{trackCode}/estado")
    TransportResponseGetStatus getStatus(@PathVariable String trackCode);
}
