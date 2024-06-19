package gsm.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.entities.Categorie; // Import Categorie entity
import gsm.entities.Produit;
import gsm.repositories.CategorieRepository; // Import CategorieRepository
import gsm.repositories.ProduitRepository;

@Service
public class ProduitServiceImpl implements ProduitService {
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private CategorieRepository categorieRepository; // Autowire CategorieRepository

    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
@Override
public List<Produit> searchProduitsByName(String nom) {
    return produitRepository.findByNomContainingIgnoreCase(nom);
}

    @Override
    public Produit createProduit(Produit produit, Long categoryId) {
        Optional<Categorie> categorieOptional = categorieRepository.findById(categoryId);
        if (categorieOptional.isPresent()) {
            Categorie category = categorieOptional.get();
            produit.setCategorie(category); // Set the category for the product
            return produitRepository.save(produit);
        } else {
            throw new NoSuchElementException("Category not found with ID: " + categoryId);
        }
    }

    @Override
    public Produit findProduitById(Long id) {
        Optional<Produit> produitOptional = produitRepository.findById(id);
        if (produitOptional.isPresent()) {
            return produitOptional.get();
        } else {
            throw new NoSuchElementException("Product not found with ID: " + id);
        }
    }

    @Override
    public Produit updateProduitById(Long id, Produit updatedProduit, Long categoryId) {
        Optional<Produit> produitOptional = produitRepository.findById(id);
        if (produitOptional.isPresent()) {
            Produit existingProduit = produitOptional.get();
            existingProduit.setNom(updatedProduit.getNom());
            // Set other fields
            Optional<Categorie> categorieOptional = categorieRepository.findById(categoryId);
            if (categorieOptional.isPresent()) {
                existingProduit.setCategorie(categorieOptional.get()); // Update the category
            } else {
                throw new NoSuchElementException("Category not found with ID: " + categoryId);
            }
            return produitRepository.save(existingProduit);
        } else {
            throw new NoSuchElementException("Product not found with ID: " + id);
        }
    }

    @Override
    public void deleteProduitById(Long id) {
        produitRepository.deleteById(id);
    }
}
