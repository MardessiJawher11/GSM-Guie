package gsm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.Marque;
@Repository
public interface MarqueRepository extends JpaRepository<Marque, Long> {

}
