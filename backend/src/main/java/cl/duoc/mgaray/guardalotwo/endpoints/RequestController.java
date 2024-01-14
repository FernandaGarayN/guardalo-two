package cl.duoc.mgaray.guardalotwo.endpoints;

import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequestDetail;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetRequestDetail;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostRequest;
import cl.duoc.mgaray.guardalotwo.service.RequestService;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestDetailCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.Request;
import cl.duoc.mgaray.guardalotwo.service.domain.RequestDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<List<ResponseGetRequest>> getAllRequests() {
        var requests = requestService.getAllRequests();
        return ResponseEntity.ok(toGetResponse(requests));
    }

    private List<ResponseGetRequest> toGetResponse(List<Request> requests) {
        return requests.stream()
                .map(this::toGetResponse)
                .toList();
    }

    private ResponseGetRequest toGetResponse(Request request) {
        return ResponseGetRequest.builder()
                .orderNumber(request.getOrderNumber())
                .date(request.getDate())
                .address(request.getAddress())
                .subsidiary(request.getSubsidiary())
                .details(toGetResponse(request.getDetails()))
                .build();
    }

    private Set<ResponseGetRequestDetail> toGetResponse(Set<RequestDetail> details) {
        return details.stream()
                .map(this::toGetResponse)
                .collect(Collectors.toSet());
    }

    private ResponseGetRequestDetail toGetResponse(RequestDetail detail) {
        return ResponseGetRequestDetail.builder()
                .sku(detail.getSku())
                .name(detail.getName())
                .price(detail.getPrice())
                .quantity(detail.getQuantity())
                .build();
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
