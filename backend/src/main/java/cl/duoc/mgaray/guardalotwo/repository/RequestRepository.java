package cl.duoc.mgaray.guardalotwo.repository;

import cl.duoc.mgaray.guardalotwo.service.cmd.RequestStatusCmd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    RequestEntity getFirstByOrderByOrderNumberDesc();

    Optional<RequestEntity> findByOrderNumber(Long orderNumber);

    Optional<RequestEntity> findByTrackCode(String trackCode);
}
