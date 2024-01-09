package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewProductCmd {
  private String sku;
  private String name;
  private String description;
  private Double price;
  private Integer stock;
  private String image;
  private boolean active;
  private long version;
}
