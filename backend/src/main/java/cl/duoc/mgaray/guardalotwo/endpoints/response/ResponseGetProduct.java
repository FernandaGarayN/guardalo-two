package cl.duoc.mgaray.guardalotwo.endpoints.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetProduct {
  private long id;
  private String sku;
  private String name;
  private String description;
  private double price;
  private int stock;
  @Setter
  private String value;
  @Setter
  private String display;
}
