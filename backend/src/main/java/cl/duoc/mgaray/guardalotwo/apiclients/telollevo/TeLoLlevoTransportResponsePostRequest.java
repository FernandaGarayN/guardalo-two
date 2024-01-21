package cl.duoc.mgaray.guardalotwo.apiclients.telollevo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeLoLlevoTransportResponsePostRequest {
    @JsonProperty("codigoSeguimiento")
    private String trackCode;
}
