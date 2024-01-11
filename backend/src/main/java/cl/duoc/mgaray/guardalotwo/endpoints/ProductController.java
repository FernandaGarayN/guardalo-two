package cl.duoc.mgaray.guardalotwo.endpoints;

import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPatchProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestPostProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseDeleteProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponseGetProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePatchProduct;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResponsePostProduct;
import cl.duoc.mgaray.guardalotwo.service.ProductService;
import cl.duoc.mgaray.guardalotwo.service.cmd.DeleteProductCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewProductCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.UpdateProductCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ResponsePostProduct> createProduct(
      @Validated @RequestBody RequestPostProduct request) {
    var newCmd = toNewCmd(request);
    var product = productService.createProduct(newCmd);
    return ResponseEntity.ok(toPostResponse(product));
  }

  @GetMapping
  public ResponseEntity<List<ResponseGetProduct>> getAllProducts() {
    var products = productService.getAllProducts();
    return ResponseEntity.ok(toGetResponse(products));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseGetProduct> getProductById(@PathVariable Long id) {
    var productById = productService.getProductById(id);
    return ResponseEntity.ok(toGetResponse(productById));
  }

  @GetMapping("/search")
  public ResponseEntity<List<ResponseGetProduct>> searchProducts(@RequestParam String term) {
    var products = productService.searchProducts(term);
    return ResponseEntity.ok(products.stream().map(this::toGetResponseWithValue).toList());
  }

  private ResponseGetProduct toGetResponseWithValue(Product product) {
    var pp = toGetResponse(product);
    pp.setValue(pp.getSku());
    pp.setDisplay(String.format("%s - %s", pp.getSku(), pp.getName()));
    return pp;
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ResponsePatchProduct> updateProduct(
      @PathVariable Long id, @Validated @RequestBody RequestPatchProduct request) {
    var cmd = toUpdateCmd(request);
    var updated = productService.updateProduct(id, cmd);
    return ResponseEntity.ok(toPatchResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDeleteProduct> deleteProduct(@PathVariable Long id) {
    var cmd = DeleteProductCmd.builder().id(id).build();
    var deleted = productService.deleteProduct(cmd);
    return ResponseEntity.ok(toDeleteResponse(deleted));
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

  private UpdateProductCmd toUpdateCmd(RequestPatchProduct request) {
    return UpdateProductCmd.builder()
        .sku(request.getSku())
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .image(request.getImage())
        .active(request.isActive())
        .build();
  }

  private ResponsePostProduct toPostResponse(Product product) {
    return ResponsePostProduct.builder().id(product.getId()).build();
  }

  private List<ResponseGetProduct> toGetResponse(List<Product> products) {
    return products.stream().map(this::toGetResponse).toList();
  }

  private ResponseGetProduct toGetResponse(Product product) {
    return ResponseGetProduct.builder()
        .id(product.getId())
        .sku(product.getSku())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .stock(product.getStock())
        .image(product.getImage())
        .active(product.isActive())
        .createdAt(product.getCreatedAt())
        .createdBy(product.getCreatedBy())
        .deletedAt(product.getDeletedAt())
        .deletedBy(product.getDeletedBy())
        .updatedAt(product.getUpdatedAt())
        .updatedBy(product.getUpdatedBy())
        .version(product.getVersion())
        .build();
  }

  private ResponsePatchProduct toPatchResponse(Product updated) {
    return ResponsePatchProduct.builder()
        .id(updated.getId())
        .sku(updated.getSku())
        .name(updated.getName())
        .description(updated.getDescription())
        .price(updated.getPrice())
        .stock(updated.getStock())
        .image(updated.getImage())
        .active(updated.isActive())
        .updatedAt(updated.getUpdatedAt())
        .updatedBy(updated.getUpdatedBy())
        .version(updated.getVersion())
        .build();
  }

  private ResponseDeleteProduct toDeleteResponse(Product deleted) {
    return ResponseDeleteProduct.builder()
        .sku(deleted.getSku())
        .name(deleted.getName())
        .description(deleted.getDescription())
        .price(deleted.getPrice())
        .stock(deleted.getStock())
        .image(deleted.getImage())
        .active(deleted.isActive())
        .deletedAt(deleted.getDeletedAt())
        .deletedBy(deleted.getDeletedBy())
        .version(deleted.getVersion())
        .build();
  }
}
