package cl.duoc.mgaray.guardalotwo.endpoints;

import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProWarehouseProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostRequestDetail;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostTransportRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetRequestDetail;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetRequestStatus;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostRequest;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostTransportRequest;
import cl.duoc.mgaray.guardalotwo.service.ProductService;
import cl.duoc.mgaray.guardalotwo.service.RequestService;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewProductCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewRequestDetailCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.RequestStatusCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.SendToTransportCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.Product;
import cl.duoc.mgaray.guardalotwo.service.domain.ProductsRequest;
import cl.duoc.mgaray.guardalotwo.service.domain.RequestDetail;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
  private final ProductService productService;
  private final RequestService requestService;

  private static Double generateTotalForInfo(ProductsRequest requestOrder) {
    return requestOrder.getDetails().stream().map(d -> d.getPrice() * d.getQuantity()).reduce(0.0, Double::sum);
  }

  private static String generateDetailForInfo(ProductsRequest requestOrder) {
    return requestOrder.getDetails().stream().map(d -> d.getSku() + " - " + d.getName() + " - " + d.getQuantity())
        .collect(Collectors.joining(", "));
  }

  private static String generateInfo(RequestPostTransportRequest request) {
    return "Número de orden de compra: " + request.getOrderNumber();
  }

  private static String generateComment(ProductsRequest requestOrder) {
    return "Fecha de entrega: " + requestOrder.getDate()
        + "\n" + "Detalles: " + generateDetailForInfo(requestOrder)
        + "\n" + "Total: " + generateTotalForInfo(requestOrder);
  }

  @PostMapping("/products")
  public ResponseEntity<ResponsePostProduct> createProduct(
      @Validated @RequestBody RequestPostProduct request) {
    var newCmd = toNewCmd(request);
    var product = productService.createProduct(newCmd);
    return ResponseEntity.ok(toPostResponse(product));
  }

  @GetMapping("/products/count")
  public ResponseEntity<Long> getTotalProducts() {
    return new ResponseEntity<>(productService.getTotalRequests(), HttpStatus.OK);
  }

  @GetMapping("/products")
  public ResponseEntity<List<ResponseGetProduct>> getAllProducts(@RequestParam(required = false) Integer pageNo,
                                                                 @RequestParam(required = false) Integer pageSize) {
    List<Product> products = null;
    if (pageNo != null && pageSize != null ) {
      var paging = PageRequest.of(pageNo, pageSize);
      products = productService.getAllProducts(paging);
    } else {
      products = productService.getAllProducts();
    }
    return ResponseEntity.ok(toGetResponse(products));
  }

  @GetMapping("/products/music-pro")
  public ResponseEntity<List<MusicProWarehouseProduct>> getAllProductsMusicPro() {
    var products = productService.getAllProductsMusicPro();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<ResponseGetProduct> getProductById(@PathVariable Long id) {
    var productById = productService.getProductById(id);
    return ResponseEntity.ok(toGetResponse(productById));
  }

  @GetMapping("/products/search")
  public ResponseEntity<List<ResponseGetProduct>> searchProducts(@RequestParam String term, @RequestParam String warehouse) {
    var products = productService.searchProducts(warehouse, term);
    return ResponseEntity.ok(products.stream().map(this::toGetResponseWithValue).toList());
  }

  @GetMapping("/requests")
  public ResponseEntity<List<ResponseGetRequest>> getAllRequests(@RequestParam(required = false) Integer pageNo,
                                                                 @RequestParam(required = false) Integer pageSize) {
    List<ProductsRequest> requests;
    if (pageNo != null && pageSize != null) {
      var paging = PageRequest.of(pageNo, pageSize);
      requests = requestService.getAllRequests(paging);
    } else {
      requests = requestService.getAllRequests();
    }
    return ResponseEntity.ok(toResponseGetRequests(requests));
  }

  @PostMapping("/requests")
  public ResponseEntity<ResponsePostRequest> post(@Validated @RequestBody RequestPostRequest request) {
    ProductsRequest created;
    var newRequestCmd = toCmd(request);
    if ("guardalotwo".equals(request.getWarehouse())) {
      created = requestService.createRequest(newRequestCmd);
    } else {
      created = requestService.createRequestMP(newRequestCmd);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(ResponsePostRequest.builder().id(created.getId()).build());
  }

  @GetMapping("/requests/count")
  public ResponseEntity<Long> getTotalRequests() {
    return new ResponseEntity<>(requestService.getTotalRequests(), HttpStatus.OK);
  }

  @PostMapping("/transport")
  public ResponseEntity<ResponsePostTransportRequest> postTransportRequest(@Validated @RequestBody RequestPostTransportRequest request) {
    var requestOrder = requestService.getByOrderNumber(request.getOrderNumber());
    var trackCode = requestService.sendToTransport(toCmd(request, requestOrder));
    requestService.setTrackCode(request.getOrderNumber(), request.getTransport(), trackCode);
    var response = ResponsePostTransportRequest.builder().trackCode(trackCode).build();
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/transport/{trackCode}/status")
  public ResponseEntity<ResponseGetRequestStatus> getRequestStatus(@PathVariable String trackCode) {
    var status = requestService.getRequestStatus(toCmd(trackCode));
    return ResponseEntity.ok(ResponseGetRequestStatus.builder().status(status).build());
  }

  private ResponsePostProduct toPostResponse(Product product) {
    return ResponsePostProduct.builder().id(product.getId()).build();
  }

  private List<ResponseGetProduct> toGetResponse(List<Product> products) {
    return products.stream().map(this::toGetResponse).toList();
  }

  private List<ResponseGetRequest> toResponseGetRequests(List<ProductsRequest> productsRequests) {
    return productsRequests.stream()
        .map(this::toResponseGetRequest)
        .toList();
  }

  private ResponseGetRequest toResponseGetRequest(ProductsRequest productsRequest) {
    return ResponseGetRequest.builder()
        .trackCode(productsRequest.getTrackCode())
        .orderNumber(productsRequest.getOrderNumber())
        .date(productsRequest.getDate())
        .address(productsRequest.getAddress())
        .subsidiary(productsRequest.getSubsidiary())
        .warehouse(getWarehouse(productsRequest.getWarehouse()))
        .transport(productsRequest.getTransport())
        .details(toResponseGetRequestDetails(productsRequest.getDetails()))
        .build();
  }

  private String getWarehouse(String warehouse) {
    return "guardalotwo".equals(warehouse) ? "Guárdalo Two" : "Music Pro";
  }

  private Set<ResponseGetRequestDetail> toResponseGetRequestDetails(Set<RequestDetail> details) {
    return details.stream()
        .map(this::toResponseGetRequestDetail)
        .collect(Collectors.toSet());
  }

  private ResponseGetRequestDetail toResponseGetRequestDetail(RequestDetail detail) {
    return ResponseGetRequestDetail.builder()
        .sku(detail.getSku())
        .name(detail.getName())
        .price(detail.getPrice())
        .quantity(detail.getQuantity())
        .build();
  }

  private ResponseGetProduct toGetResponse(Product product) {
    return ResponseGetProduct.builder()
        .id(product.getId())
        .sku(product.getSku())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .stock(product.getStock())
        .build();
  }

  private ResponseGetProduct toGetResponseWithValue(Product product) {
    var pp = toGetResponse(product);
    pp.setValue(pp.getSku());
    pp.setDisplay(String.format("%s - %s", pp.getSku(), pp.getName()));
    return pp;
  }

  private NewProductCmd toNewCmd(RequestPostProduct request) {
    return NewProductCmd.builder()
        .sku(request.getSku())
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .image(request.getImage())
        .active(request.isActive())
        .build();
  }

  private RequestStatusCmd toCmd(String trackCode) {
    return RequestStatusCmd.builder()
        .trackCode(trackCode)
        .build();
  }

  private NewRequestCmd toCmd(RequestPostRequest request) {
    return NewRequestCmd.builder()
        .warehouse(request.getWarehouse())
        .address(request.getAddress())
        .subsidiary(request.getSubsidiary())
        .details(toCmd(request.getDetails()))
        .build();
  }

  private List<NewRequestDetailCmd> toCmd(List<RequestPostRequestDetail> details) {
    return details.stream()
        .map(
            r -> NewRequestDetailCmd.builder()
                .id(r.getId())
                .sku(r.getSku())
                .name(r.getName())
                .price(r.getPrice())
                .description(r.getDescription())
                .quantity(r.getQuantity())
                .build()
        )
        .toList();
  }

  private SendToTransportCmd toCmd(RequestPostTransportRequest request, ProductsRequest requestOrder) {
    return SendToTransportCmd.builder()
        .orderNumber(request.getOrderNumber().toString())
        .transport(request.getTransport())
        .info(generateInfo(request))
        .comment(generateComment(requestOrder))
        .destination(requestOrder.getSubsidiary())
        .destinationAddress(requestOrder.getAddress())
        .build();
  }
}
