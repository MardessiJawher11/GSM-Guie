package gsm.service;

import java.util.List;
import gsm.entities.Categorie;
import gsm.entities.Produit;

import org.springframework.web.multipart.MultipartFile;

public interface CategorieService {
    List<Categorie> getAllCategories();
    Categorie findCategorieById(Long id);
    Categorie createCategorie(Categorie categorie);
    Categorie updateCategorieById(Long id, Categorie updatedCategorie);
    void deleteCategorie(Long id);
    public List<Produit> getProduitsByCategorie(Long categorieId);
}
