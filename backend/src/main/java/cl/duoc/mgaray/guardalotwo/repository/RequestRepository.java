package cl.duoc.mgaray.guardalotwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    RequestEntity getFirstByOrderByOrderNumberDesc();

    Optional<RequestEntity> findByOrderNumber(Long orderNumber);
}
