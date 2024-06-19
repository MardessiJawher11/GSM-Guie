package gsm.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.entities.Categorie;
import gsm.entities.Produit;
import gsm.repositories.CategorieRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategorieServiceImpl implements CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
    public List<Produit> getProduitsByCategorie(Long categorieId) {
        Categorie categorie = categorieRepository.findById(categorieId).orElse(null);
        if (categorie != null) {
            return categorie.getProduits();
        }
        return null;
    }
    public Categorie findCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categorie not found with ID: " + id));
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorieById(Long id, Categorie updatedCategorie) {
        Optional<Categorie> categorieOptional = categorieRepository.findById(id);
        if (categorieOptional.isPresent()) {
            Categorie existingCategorie = categorieOptional.get();
            if (updatedCategorie.getName() != null) {
                existingCategorie.setName(updatedCategorie.getName());
            }
            return categorieRepository.save(existingCategorie);
        } else {
            throw new NoSuchElementException("Categorie not found with ID: " + id);
        }
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }



   
}
