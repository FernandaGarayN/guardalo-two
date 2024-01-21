package cl.duoc.mgaray.guardalotwo.apiclients.musicpro;

import cl.duoc.mgaray.guardalotwo.apiclients.common.CommonFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "musicpro-client",
    url = "https://musicpro.bemtorres.win/api/v1",
    configuration = CommonFeignConfig.class)
public interface MusicProClient {
  @GetMapping("/bodega/producto")
  MusicProResponseGetProducts getWarehouseProducts();

  @PostMapping("/bodega/solicitud")
  MusicProResponsePostWarehouseRequest postWarehouseRequest(@RequestBody MusicProRequestPostWarehouseRequest request);

  @PostMapping("/transporte/solicitud")
  MusicProResponsePostTransportRequest postTransportRequest(@RequestBody MusicProRequestPostTransportRequest request);

  @GetMapping("/transporte/seguimiento/{trackCode}")
  MusicProResponseGetRequestStatus getRequestStatus(@PathVariable String trackCode);
}
