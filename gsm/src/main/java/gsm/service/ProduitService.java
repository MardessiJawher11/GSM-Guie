package gsm.service;

import java.util.List;

import gsm.entities.Produit;

public interface ProduitService {
    public List<Produit> getAllProduits();
    public Produit createProduit(Produit produit, Long categoryId); // Accept category ID
    public Produit findProduitById(Long id);
    public Produit updateProduitById(Long id, Produit updatedProduit, Long categoryId); // Accept category ID
    public void deleteProduitById(Long id);
    public List<Produit> searchProduitsByName(String nom);
}
