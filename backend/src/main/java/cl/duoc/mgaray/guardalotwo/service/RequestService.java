package cl.duoc.mgaray.guardalotwo.service;

import cl.duoc.mgaray.guardalotwo.apiclients.transport.TransportClient;
import cl.duoc.mgaray.guardalotwo.apiclients.transport.TransportRequestPostRequest;
import cl.duoc.mgaray.guardalotwo.repository.*;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestDetailCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.RequestStatusCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.SendToTransportCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.Request;
import cl.duoc.mgaray.guardalotwo.service.domain.RequestDetail;
import cl.duoc.mgaray.guardalotwo.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final ProductRepository productRepository;
    private final TransportClient transportClient;

    @Transactional
    public Request createRequest(NewRequestCmd cmd) {
        var found = requestRepository.getFirstByOrderByOrderNumberDesc();
        var saved = requestRepository.save(toEntity(cmd, found.getOrderNumber()));
        var savedDetails = requestDetailRepository.saveAll(toEntities(cmd.getDetails(), saved));
        return toDomain(saved, new HashSet<>(savedDetails));
    }

    @Transactional(readOnly = true)
    public List<Request> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .map(r -> toDomain(r, r.getDetails()))
                .toList();
    }

    public String sendToTransport(SendToTransportCmd cmd){
        var response = transportClient.postRequest(toClientRequest(cmd));
        return response.getTrackCode();
    }

    private TransportRequestPostRequest toClientRequest(SendToTransportCmd cmd) {
        return TransportRequestPostRequest.builder()
                .description("Solicitud de transporte")
                .destination(cmd.getDestination())
                .destinationAddress(cmd.getDestinationAddress())
                .origin("BODEGA")
                .originAddress("DIRECCION DE LA BODEGA")
                .orderNumber(cmd.getOrderNumber())
                .build();
    }

    @Transactional
    public void setTrackCode(Long orderNumber, String trackCode) {
        var request = requestRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        request.setTrackCode(trackCode);
    }

    @Transactional
    public String getRequestStatus(RequestStatusCmd cmd) {
        var response = transportClient.getStatus(cmd.getTrackCode());
        return response.getStatus();
    }

    private List<RequestDetailEntity> toEntities(List<NewRequestDetailCmd> details, RequestEntity saved) {
        return details.stream()
                .map(d -> toEntity(d, saved))
                .toList();
    }

    private RequestEntity toEntity(NewRequestCmd cmd, Long lastOrderNumber) {
        return RequestEntity.builder()
                .address(cmd.getAddress())
                .subsidiary(cmd.getSubsidiary())
                .orderNumber(++lastOrderNumber)
                .date(LocalDate.now())
                .build();
    }

    private RequestDetailEntity toEntity(NewRequestDetailCmd cmd, RequestEntity saved) {
        ProductEntity product = productRepository.findBySkuOrName(cmd.getSku(), cmd.getSku())
                .orElseThrow(() -> new NotFoundException("Product not found with sku " + cmd.getSku()));
        return RequestDetailEntity.builder()
                .request(saved)
                .product(product)
                .quantity(cmd.getQuantity())
                .build();
    }

    private Request toDomain(RequestEntity request, Set<RequestDetailEntity> details) {
        return Request.builder()
                .id(request.getId())
                .address(request.getAddress())
                .subsidiary(request.getSubsidiary())
                .date(request.getDate())
                .orderNumber(request.getOrderNumber())
                .trackCode(request.getTrackCode())
                .details(toDomain(details))
                .build();
    }
    private Set<RequestDetail> toDomain(Set<RequestDetailEntity> details) {
        return details.stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());
    }

    private RequestDetail toDomain(RequestDetailEntity detail) {
        return RequestDetail.builder()
                .sku(detail.getProduct().getSku())
                .price(detail.getProduct().getPrice())
                .name(detail.getProduct().getName())
                .quantity(detail.getQuantity())
                .build();
    }
}
