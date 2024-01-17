package cl.duoc.mgaray.guardalotwo.apiclients.warehouse;

import cl.duoc.mgaray.guardalotwo.config.feign.FeignTransportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "warehouse-client",
    url = "https://musicpro.bemtorres.win/api/v1/bodega",
    configuration = FeignTransportConfig.class)
public interface WarehouseClient {
  @GetMapping("/producto")
  WarehouseResponseGetProducts getProducts();
}
