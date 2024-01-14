package cl.duoc.mgaray.guardalotwo.endpoints;

import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequestDetail;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostRequest;
import cl.duoc.mgaray.guardalotwo.service.RequestService;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestDetailCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ResponsePostRequest> post(@Validated @RequestBody RequestPostRequest request) {
        var created = requestService.createRequest(toCmd(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponsePostRequest.builder().id(created.getId()).build());
    }

    private NewRequestCmd toCmd(RequestPostRequest request) {
        return NewRequestCmd.builder()
                .address(request.getAddress())
                .subsidiary(request.getSubsidiary())
                .details(toCmd(request.getDetails()))
                .build();
    }

    private List<NewRequestDetailCmd> toCmd(List<RequestPostRequestDetail> details) {
        return details.stream()
                .map(
                        r -> NewRequestDetailCmd.builder()
                                .sku(r.getSku())
                                .quantity(r.getQuantity())
                                .build()
                )
                .toList();
    }
}
