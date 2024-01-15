package cl.duoc.mgaray.guardalotwo.apiclients.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransportResponseGetStatus {
    @JsonProperty("estado")
    private String status;
}
