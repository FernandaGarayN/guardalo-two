package cl.duoc.mgaray.guardalotwo.endpoints.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseGetProduct {
  private long id;
  private String sku;
  private String name;
  private String description;
  private double price;
  private int stock;
  private String image;
  private boolean active;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String deletedBy;
  private String updatedBy;
  private String createdBy;
  private long version;
}
