package cl.duoc.mgaray.guardalotwo.apiclients.warehouse;

import cl.duoc.mgaray.guardalotwo.config.feign.FeignTransportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "warehouse-client",
    url = "https://musicpro.bemtorres.win/api/v1",
    configuration = FeignTransportConfig.class)
public interface WarehouseClient {
  @GetMapping("/bodega/producto")
  WarehouseResponseGetProducts getWarehouseProducts();
  @PostMapping("/transporte/solicitud")
  WarehouseResponsePostTransportRequest postTransportRequest(@RequestBody WarehouseRequestPostTransportRequest request);
  @GetMapping("/transporte/seguimiento/{trackCode}")
  WarehouseResponseGetRequestStatus getRequestStatus(@PathVariable String trackCode);
}
