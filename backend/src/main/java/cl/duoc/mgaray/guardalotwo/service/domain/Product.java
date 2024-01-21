package cl.duoc.mgaray.guardalotwo.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Product {
  private long id;
  private String sku;
  private String name;
  private String description;
  private double price;
  private int stock;
}
