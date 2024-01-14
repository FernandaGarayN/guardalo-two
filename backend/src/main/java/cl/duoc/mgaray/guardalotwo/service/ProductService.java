package cl.duoc.mgaray.guardalotwo.service;

import cl.duoc.mgaray.guardalotwo.repository.ProductEntity;
import cl.duoc.mgaray.guardalotwo.repository.ProductRepository;
import cl.duoc.mgaray.guardalotwo.service.cmd.DeleteProductCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.NewProductCmd;
import cl.duoc.mgaray.guardalotwo.service.cmd.UpdateProductCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.Product;
import cl.duoc.mgaray.guardalotwo.service.exception.FoundException;
import cl.duoc.mgaray.guardalotwo.service.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;

  @Transactional
  public Product createProduct(NewProductCmd cmd) {
    productRepository
        .findBySkuOrName(cmd.getSku(), cmd.getName())
        .ifPresent(
            p -> {
              throw new FoundException("Product already exists");
            });
    ProductEntity saved = productRepository.save(toEntity(cmd));
    return toDomain(saved);
  }

  @Transactional(readOnly = true)
  public List<Product> getAllProducts() {
    return productRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Transactional(readOnly = true)
  public Product getProductById(Long id) {
    return toDomain(
        productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found")));
  }

  public List<Product> searchProducts(String term) {
    List<ProductEntity> founds =
        productRepository.findBySkuContainingOrNameContainingOrDescriptionContaining(term, term, term);
    return toDomain(founds);
  }

  @Transactional
  public Product updateProduct(Long id, UpdateProductCmd cmd) {

    productRepository
        .findBySkuOrName(cmd.getSku(), cmd.getName())
        .ifPresent(
            p -> {
              throw new FoundException("Product with sku or name already exists");
            });

    ProductEntity found = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    update(cmd, found);
    ProductEntity saved = productRepository.save(found);
    return toDomain(saved);
  }

  @Transactional
  public Product deleteProduct(DeleteProductCmd cmd) {
    ProductEntity found =
        productRepository
            .findById(cmd.getId())
            .orElseThrow(() -> new NotFoundException("Product not found"));
    productRepository.delete(found);
    return toDomain(found);
  }

  private void update(UpdateProductCmd cmd, ProductEntity found) {
    if (cmd.getSku() != null) {
      found.setSku(cmd.getSku());
    }

    if (cmd.getName() != null) {
      found.setName(cmd.getName());
    }

    if (cmd.getDescription() != null) {
      found.setDescription(cmd.getDescription());
    }

    found.setPrice(cmd.getPrice());

    found.setStock(cmd.getStock());
  }

  private List<Product> toDomain(List<ProductEntity> products) {
    return products.stream().map(this::toDomain).toList();
  }

  private Product toDomain(ProductEntity saved) {
    return Product.builder()
        .id(saved.getId())
        .sku(saved.getSku())
        .name(saved.getName())
        .description(saved.getDescription())
        .price(saved.getPrice())
        .stock(saved.getStock())
        .build();
  }

  private ProductEntity toEntity(NewProductCmd cmd) {
    return ProductEntity.builder()
        .sku(cmd.getSku())
        .name(cmd.getName())
        .description(cmd.getDescription())
        .price(cmd.getPrice())
        .stock(cmd.getStock())
        .build();
  }
}
