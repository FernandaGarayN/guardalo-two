package cl.duoc.mgaray.guardalotwo.service;

import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProClient;
import cl.duoc.mgaray.guardalotwo.apiclients.musicpro.MusicProWarehouseProduct;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  public static final String NOT_FOUND_EXCEPTION_MESSAGE_PRODUCT = "Product not found";
  public static final String FOUND_EXCEPTION_MESSAGE_PRODUCT = "Product already exists";
  private final ProductRepository productRepository;
  private final MusicProClient musicProClient;

  @Transactional
  public Product createProduct(NewProductCmd cmd) {
    productRepository
        .findBySkuOrName(cmd.getSku(), cmd.getName())
        .ifPresent(
            p -> {
              throw new FoundException(FOUND_EXCEPTION_MESSAGE_PRODUCT);
            });
    ProductEntity saved = productRepository.save(toEntity(cmd));
    return toDomain(saved);
  }

  @Transactional(readOnly = true)
  public List<Product> getAllProducts(Pageable paging) {
    return productRepository.findAll(paging).stream().map(this::toDomain).toList();
  }

  private Product toDomain(MusicProWarehouseProduct musicProWarehouseProduct) {
    var stock = musicProWarehouseProduct.getStock();
    if (stock == null) {
      stock = musicProWarehouseProduct.getCantidad();
      if (stock == null) {
        stock = 100;
        log.warn("Product {}-{} [id={}] has no stock. Default stock => {}", musicProWarehouseProduct.getCode(), musicProWarehouseProduct.getName(),
            musicProWarehouseProduct.getId(), 100);
      }
    }
    return Product.builder()
        .id(musicProWarehouseProduct.getId())
        .sku(musicProWarehouseProduct.getCode())
        .name(musicProWarehouseProduct.getName())
        .description(musicProWarehouseProduct.getDescription())
        .price(musicProWarehouseProduct.getRawPrice())
        .stock(stock)
        .build();
  }


  @Transactional(readOnly = true)
  public List<MusicProWarehouseProduct> getAllProductsMusicPro() {
    return musicProClient.getWarehouseProducts().getProducts();
  }

  @Transactional(readOnly = true)
  public Product getProductById(Long id) {
    return toDomain(
        productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_EXCEPTION_MESSAGE_PRODUCT)));
  }

  public List<Product> searchProducts(String transportEnterprise, String term) {
    if ("guardalotwo".equals(transportEnterprise)) {
      List<ProductEntity> founds =
          productRepository.findBySkuContainingOrNameContainingOrDescriptionContaining(term, term, term);
      return toDomain(founds);
    } else {
      var productsMusicPro = musicProClient.getWarehouseProducts()
          .getProducts()
          .stream()
          .filter(p -> p.getCode().contains(term) || p.getName().contains(term) || p.getDescription().contains(term))
          .toList();
      return productsMusicPro.stream()
          .map(this::toDomain)
          .toList();
    }
  }

  @Transactional
  public Product updateProduct(Long id, UpdateProductCmd cmd) {

    productRepository
        .findBySkuOrName(cmd.getSku(), cmd.getName())
        .ifPresent(
            p -> {
              throw new FoundException("Product with sku or name already exists");
            });

    ProductEntity found = productRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_EXCEPTION_MESSAGE_PRODUCT));
    update(cmd, found);
    ProductEntity saved = productRepository.save(found);
    return toDomain(saved);
  }

  @Transactional
  public Product deleteProduct(DeleteProductCmd cmd) {
    ProductEntity found =
        productRepository
            .findById(cmd.getId())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_EXCEPTION_MESSAGE_PRODUCT));
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

  public Long getTotalRequests() {
    return productRepository.count();
  }
}
