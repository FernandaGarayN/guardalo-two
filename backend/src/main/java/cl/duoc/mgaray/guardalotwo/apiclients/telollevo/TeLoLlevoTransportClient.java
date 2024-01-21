package cl.duoc.mgaray.guardalotwo.apiclients.telollevo;

import cl.duoc.mgaray.guardalotwo.apiclients.common.CommonFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "telollevo-client",
    url = "http://192.168.100.27:8080",
    configuration = CommonFeignConfig.class)
public interface TeLoLlevoTransportClient {
  @PostMapping("/api/v1/solicitudes")
  TeLoLlevoTransportResponsePostRequest postRequest(@RequestBody TeLoLlevoTransportRequestPostRequest request);

  @GetMapping("/api/v1/solicitudes/{trackCode}/estado")
  TeloLlevoTransportResponseGetStatus getStatus(@PathVariable String trackCode);
}
