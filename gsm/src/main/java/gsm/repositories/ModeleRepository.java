package gsm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gsm.entities.Modele;
@Repository
public interface ModeleRepository extends JpaRepository<Modele,Long> {
	  @Query("SELECT m FROM Modele m WHERE m.marque.id = :marqueId")
	    List<Modele> findByMarqueId(Long marqueId);
}
