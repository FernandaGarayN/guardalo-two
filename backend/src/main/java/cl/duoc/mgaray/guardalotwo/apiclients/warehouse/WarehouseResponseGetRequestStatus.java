package cl.duoc.mgaray.guardalotwo.apiclients.warehouse;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseGetRequestStatus {
  private Integer status;
  private Map<String, Object> result;

  public String getStatus() {
    return result.get("estado").toString();
  }
}
