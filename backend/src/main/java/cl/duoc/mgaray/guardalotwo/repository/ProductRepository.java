package cl.duoc.mgaray.guardalotwo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  Optional<ProductEntity> findBySkuOrName(String sku, String name);
  List<ProductEntity> findBySkuContainingOrNameContainingOrDescriptionContaining(String sku, String name, String description);
}
