package cl.duoc.mgaray.guardalotwo.service;

import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProClient;
import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProRequestPostTransportRequest;
import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProRequestPostWarehouseRequest;
import cl.duoc.mgaray.guardalotwo.apiclients.telollevo.TeLoLlevoTransportClient;
import cl.duoc.mgaray.guardalotwo.apiclients.telollevo.TeLoLlevoTransportRequestPostRequest;
import cl.duoc.mgaray.guardalotwo.repository.ProductEntity;
import cl.duoc.mgaray.guardalotwo.repository.ProductRepository;
import cl.duoc.mgaray.guardalotwo.repository.RequestDetailEntity;
import cl.duoc.mgaray.guardalotwo.repository.RequestDetailMPEntity;
import cl.duoc.mgaray.guardalotwo.repository.RequestDetailMPRepository;
import cl.duoc.mgaray.guardalotwo.repository.RequestDetailRepository;
import cl.duoc.mgaray.guardalotwo.repository.RequestEntity;
import cl.duoc.mgaray.guardalotwo.repository.RequestRepository;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestDetailCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.RequestStatusCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.SendToTransportCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.ProductsRequest;
import cl.duoc.mgaray.guardalotwo.service.domain.RequestDetail;
import cl.duoc.mgaray.guardalotwo.service.exception.NotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestService {
  public static final String PIVOT_TRANSPORT_ENTERPRISE = "telollevo";
  private final RequestRepository requestRepository;
  private final RequestDetailRepository requestDetailRepository;
  private final RequestDetailMPRepository requestDetailMPRepository;
  private final ProductRepository productRepository;
  private final TeLoLlevoTransportClient teLoLlevoTransportClient;
  private final MusicProClient musicProClient;

  @Transactional
  public ProductsRequest createRequest(NewRequestCmd cmd) {
    var found = requestRepository.getFirstByOrderByOrderNumberDesc();
    var saved = requestRepository.save(toEntity(cmd, found.getOrderNumber()));
    var savedDetails = requestDetailRepository.saveAll(toEntities(cmd.getDetails(), saved));
    saved.setDetails(new HashSet<>(savedDetails));
    return toDomain(saved);
  }

  @Transactional
  public ProductsRequest createRequestMP(NewRequestCmd cmd) {
    var found = requestRepository.getFirstByOrderByOrderNumberDesc();
    var saved = requestRepository.save(toEntity(cmd, found.getOrderNumber()));
    var savedDetails = requestDetailMPRepository.saveAll(toEntitiesMP(cmd.getDetails(), saved));
    musicProClient.postWarehouseRequest(toClientRequest(cmd));
    return toDomainMP(saved, new HashSet<>(savedDetails));
  }

  @Transactional(readOnly = true)
  public List<ProductsRequest> getAllRequests(PageRequest paging) {
    return requestRepository.findAll(paging)
        .stream()
        .map(this::toDomain)
        .toList();
  }

  @Transactional
  public String sendToTransport(SendToTransportCmd cmd) {
    if (PIVOT_TRANSPORT_ENTERPRISE.equalsIgnoreCase(cmd.getTransport())) {
      var response = teLoLlevoTransportClient.postRequest(toClientRequest(cmd));
      return response.getTrackCode();
    } else {
      var response = musicProClient.postTransportRequest(toClientTransportRequest(cmd));
      return response.getTrackCode();
    }
  }

  @Transactional
  public void setTrackCode(Long orderNumber, String trasnport, String trackCode) {
    var request = requestRepository.findByOrderNumber(orderNumber)
        .orElseThrow(() -> new NotFoundException("Request not found"));
    request.setTransport(trasnport);
    request.setTrackCode(trackCode);
    requestRepository.save(request);
  }

  @Transactional
  public String getRequestStatus(RequestStatusCmd cmd) {
    var request = requestRepository.findByTrackCode(cmd.getTrackCode())
        .orElseThrow(() -> new NotFoundException("Request not found"));

    if (PIVOT_TRANSPORT_ENTERPRISE.equalsIgnoreCase(request.getTransport())) {
      var response = teLoLlevoTransportClient.getStatus(cmd.getTrackCode());
      return response.getStatus();
    } else {
      var response = musicProClient.getRequestStatus(cmd.getTrackCode());
      return response.getStatus();
    }
  }

  @Transactional(readOnly = true)
  public ProductsRequest getByOrderNumber(Long orderNumber) {
    return requestRepository.findByOrderNumber(orderNumber)
        .map(this::toDomain)
        .orElseThrow(() -> new NotFoundException("Request not found"));
  }

  @Transactional(readOnly = true)
  public long getTotalRequests() {
    return requestRepository.count();
  }

  private MusicProRequestPostWarehouseRequest toClientRequest(NewRequestCmd cmd) {
    return MusicProRequestPostWarehouseRequest.builder()
        .enterpriseName(cmd.getSubsidiary())
        .enterpriseAddress(cmd.getAddress())
        .products(cmd.getDetails().stream().map(this::toClientProduct).toList())
        .build();
  }

  private MusicProRequestPostWarehouseRequest.Product toClientProduct(NewRequestDetailCmd newRequestDetailCmd) {
    return MusicProRequestPostWarehouseRequest.Product.builder()
        .productId(newRequestDetailCmd.getId().toString())
        .quantity(newRequestDetailCmd.getQuantity())
        .build();
  }

  private MusicProRequestPostTransportRequest toClientTransportRequest(SendToTransportCmd cmd) {
    return MusicProRequestPostTransportRequest.builder()
        .comment(cmd.getComment())
        .info(cmd.getInfo())
        .destinationName(cmd.getDestination())
        .destinationAddress(cmd.getDestinationAddress())
        .originName("Guardalo Two")
        .originAddress("Av. Providencia 1234, Santiago")
        .build();
  }

  private TeLoLlevoTransportRequestPostRequest toClientRequest(SendToTransportCmd cmd) {
    return TeLoLlevoTransportRequestPostRequest.builder()
        .description("Solicitud de transporte")
        .destination(cmd.getDestination())
        .destinationAddress(cmd.getDestinationAddress())
        .origin("Guardalo Two")
        .originAddress("Av. Providencia 1234, Santiago")
        .orderNumber(cmd.getOrderNumber())
        .build();
  }

  private List<RequestDetailEntity> toEntities(List<NewRequestDetailCmd> details, RequestEntity saved) {
    return details.stream()
        .map(d -> toEntity(d, saved))
        .toList();
  }

  private List<RequestDetailMPEntity> toEntitiesMP(List<NewRequestDetailCmd> details, RequestEntity saved) {
    return details.stream()
        .map(d -> toEntityMP(d, saved))
        .toList();
  }

  private RequestEntity toEntity(NewRequestCmd cmd, Long lastOrderNumber) {
    return RequestEntity.builder()
        .warehouse(cmd.getWarehouse())
        .address(cmd.getAddress())
        .subsidiary(cmd.getSubsidiary())
        .orderNumber(++lastOrderNumber)
        .date(LocalDate.now())
        .build();
  }

  private RequestDetailMPEntity toEntityMP(NewRequestDetailCmd cmd, RequestEntity saved) {
    return RequestDetailMPEntity.builder()
        .request(saved)
        .sku(cmd.getSku())
        .name(cmd.getName())
        .description(cmd.getDescription())
        .price(cmd.getPrice())
        .quantity(cmd.getQuantity())
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

  private ProductsRequest toDomain(RequestEntity request) {
    ProductsRequest build = ProductsRequest.builder()
        .id(request.getId())
        .warehouse(request.getWarehouse())
        .address(request.getAddress())
        .subsidiary(request.getSubsidiary())
        .date(request.getDate())
        .orderNumber(request.getOrderNumber())
        .trackCode(request.getTrackCode())
        .transport(findTransport(request.getTransport()))
        .build();

    if ("musicpro".equals(request.getWarehouse())) {
      build.setDetails(request.getDetailsMP().stream().map(this::toDomain).collect(Collectors.toSet()));
    } else {
      build.setDetails(request.getDetails().stream().map(this::toDomain).collect(Collectors.toSet()));
    }
    return build;
  }

  private String findTransport(String transport) {
    if ("musicpro".equals(transport)) {
      return "Music Pro";
    } else if (PIVOT_TRANSPORT_ENTERPRISE.equals(transport)) {
      return "Te Lo Llevo";
    } else {
      return transport;
    }
  }

  private RequestDetail toDomain(RequestDetailMPEntity requestDetailMPEntity) {
    return RequestDetail.builder()
        .id(requestDetailMPEntity.getId())
        .sku(requestDetailMPEntity.getSku())
        .price(requestDetailMPEntity.getPrice())
        .name(requestDetailMPEntity.getName())
        .quantity(requestDetailMPEntity.getQuantity())
        .build();
  }

  private ProductsRequest toDomainMP(RequestEntity request, Set<RequestDetailMPEntity> details) {
    return ProductsRequest.builder()
        .id(request.getId())
        .address(request.getAddress())
        .subsidiary(request.getSubsidiary())
        .date(request.getDate())
        .orderNumber(request.getOrderNumber())
        .trackCode(request.getTrackCode())
        .transport(request.getTransport())
        .details(toDomainMP(details))
        .build();
  }

  private Set<RequestDetail> toDomainMP(Set<RequestDetailMPEntity> details) {
    return details.stream()
        .map(this::toDomainMP)
        .collect(Collectors.toSet());
  }

  private RequestDetail toDomainMP(RequestDetailMPEntity requestDetailMPEntity) {
    return RequestDetail.builder()
        .sku(requestDetailMPEntity.getSku())
        .price(requestDetailMPEntity.getPrice())
        .name(requestDetailMPEntity.getName())
        .quantity(requestDetailMPEntity.getQuantity())
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
