package cl.duoc.mgaray.guardalotwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailMPRepository extends JpaRepository<RequestDetailMPEntity, Long> {

}
