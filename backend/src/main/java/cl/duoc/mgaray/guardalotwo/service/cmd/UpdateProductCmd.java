package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProductCmd {
  private String sku;
  private String name;
  private String description;
  private double price;
  private int stock;
  private String image;
  private boolean active;
}
