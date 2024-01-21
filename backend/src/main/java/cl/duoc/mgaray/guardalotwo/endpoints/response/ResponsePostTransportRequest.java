package cl.duoc.mgaray.guardalotwo.endpoints.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponsePostTransportRequest {
  private String trackCode;
}
