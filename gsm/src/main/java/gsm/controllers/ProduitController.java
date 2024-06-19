package gsm.controllers;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gsm.entities.Categorie;
import gsm.entities.Produit;
import gsm.repositories.CategorieRepository;
import gsm.repositories.ProduitRepository;
import gsm.service.ProduitService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/produit")
public class ProduitController {
	@Autowired
	private ProduitRepository produitRepository;
	@Autowired
	private ProduitService produitService;
	@Autowired
	private CategorieRepository categorieRepository;
	@GetMapping
	public List<Produit>getAllProduits(){
		return produitService.getAllProduits();
	}
	@GetMapping(path="/{id}")
	public ResponseEntity<Produit>findProduitById(@PathVariable Long id){
		Produit produit=produitService.findProduitById(id);
		if (produit == null) {
			return new ResponseEntity<Produit>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Produit>(produit,HttpStatus.OK);
		}
		
	}
	@GetMapping("/search")
	public ResponseEntity<List<Produit>> searchProduitsByName(@RequestParam("nom") String nom) {
	    List<Produit> produits = produitService.searchProduitsByName(nom);
	    if (produits.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } else {
	        return new ResponseEntity<>(produits, HttpStatus.OK);
	    }
	}
@PostMapping("/image")
	public Produit uploadImage(
		    @RequestParam("file") MultipartFile file,
		    @RequestParam("nom") String nom,
		    @RequestParam("color") String color,
		    @RequestParam("prix") Double prix,
		    @RequestParam("description") String description,
		    @RequestParam("etat") String etat,
		    @RequestParam("categoryId") Long categoryId){
		    try {
		        Produit produit = new Produit();
		        produit.setNom(nom);
		        produit.setColor(color);
		        produit.setPrix(prix);
		        produit.setDescription(description);
		        produit.setEtat(etat);
		        produit.setEtat(etat);
		        produit.setImage(file.getBytes());
		        Categorie categorie = categorieRepository.findById(categoryId)
		                .orElseThrow(() -> new NoSuchElementException("Category not found with ID: " + categoryId));
		            produit.setCategorie(categorie);		    
		            return produitRepository.save(produit);
		    } catch (Exception e) {
		        throw new RuntimeException("Failed to upload image!");
		    }
		}



	@GetMapping("/{id}/image")
	public ResponseEntity<byte[]> getProduitImage(@PathVariable Long id) {
	    Produit produit = produitService.findProduitById(id);
	    if (produit != null && produit.getImage() != null) {
	        return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_JPEG).body(produit.getImage());
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	@PutMapping("/{id}")
	public ResponseEntity<Produit> updateProduitById(@PathVariable Long id,@RequestParam("categoryId") Long categoryId, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("nom") String nom, @RequestParam("color") String color, @RequestParam("prix") Double prix, @RequestParam("description") String description, @RequestParam("etat") String etat) {
	    Optional<Produit> optionalProduit = produitRepository.findById(id);
	    if (optionalProduit.isPresent()) {
	        try {
	            Produit existingProduit = optionalProduit.get();
	            existingProduit.setNom(nom);
	            existingProduit.setColor(color);
	            existingProduit.setPrix(prix);
	            existingProduit.setDescription(description);
	            existingProduit.setEtat(etat);
	            if (file != null && !file.isEmpty()) {
	                existingProduit.setImage(file.getBytes());
	            }
	            Categorie categorie = categorieRepository.findById(categoryId)
		                .orElseThrow(() -> new NoSuchElementException("Category not found with ID: " + categoryId));
	            existingProduit.setCategorie(categorie);
	            Produit updatedProduitEntity = produitRepository.save(existingProduit);
	            return new ResponseEntity<>(updatedProduitEntity, HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping(path="/supprimer/{id}")
	public void deleteProduit(@PathVariable Long id) {
		produitService.deleteProduitById(id);
	}
	
}
