package gsm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gsm.entities.Categorie;
import gsm.entities.Produit;
import gsm.repositories.CategorieRepository;
import gsm.service.CategorieService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/categories")
public class CategorieController {
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private CategorieService categorieService;
	@GetMapping
	public List<Categorie> getAllCategories(){
		return categorieService.getAllCategories();
	}
	@GetMapping(path="/{id}")
	public ResponseEntity<Categorie>findCategorieById(@PathVariable Long id){
		Categorie categorie=categorieService.findCategorieById(id);
		if (categorie==null) {
			return new ResponseEntity<Categorie>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Categorie>(categorie,HttpStatus.OK);
		}
	}
	@PostMapping
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie){
		try{
		Categorie createdCategorie =categorieService.createCategorie(categorie);
			if (createdCategorie!=null) {
				return new ResponseEntity<>(createdCategorie, HttpStatus.CREATED);
			}
			  else {
	             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	         }
		}
		catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping(path="/{id}")
	public ResponseEntity<Categorie>updateCategorieById(@PathVariable Long id, @RequestBody Categorie updatedCategorie){
		Optional<Categorie>optionalCategorie = categorieRepository.findById(id);
		if(optionalCategorie.isPresent()) {
			Categorie existingCategorie=optionalCategorie.get();
			existingCategorie.setName(updatedCategorie.getName());
			Categorie updatedCategoorie=categorieRepository.save(existingCategorie);
			return ResponseEntity.ok(updatedCategoorie);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping(path="/supprimer/{id}")
	public void deleteCategorie(@PathVariable Long id) {
		categorieService.deleteCategorie(id);
	}
	 @GetMapping("/{id}/products")
	    public ResponseEntity<List<Produit>> getProductsByCategory(@PathVariable Long id) {
	        List<Produit> produits = categorieService.getProduitsByCategorie(id);
	        if (produits.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } else {
	            return new ResponseEntity<>(produits, HttpStatus.OK);
	        }
	    }
}
