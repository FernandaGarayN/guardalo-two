package cl.duoc.mgaray.guardalotwo.apiclients.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransportRequestPostRequest {
    @JsonProperty("numeroDeOrden")
    private String orderNumber;
    @JsonProperty("origen")
    private String origin;
    @JsonProperty("destino")
    private String destination;
    @JsonProperty("descripcion")
    private String description;
    @JsonProperty("direccionOrigen")
    private String originAddress;
    @JsonProperty("direccionDestino")
    private String destinationAddress;
}
