package gsm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.Produit;
@Repository
public interface ProduitRepository extends JpaRepository<Produit,Long> {
	List<Produit> findByNomContainingIgnoreCase(String nom);

}
