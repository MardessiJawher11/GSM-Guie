package gsm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.Categorie;
@Repository
public interface CategorieRepository extends JpaRepository <Categorie,Long>{

}
