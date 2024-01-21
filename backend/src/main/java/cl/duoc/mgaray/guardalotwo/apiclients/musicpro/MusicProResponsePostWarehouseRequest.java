package cl.duoc.mgaray.guardalotwo.apiclients.musicpro;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MusicProResponsePostWarehouseRequest {
  private String message;
  @JsonProperty("contenido")
  private String content;
}
