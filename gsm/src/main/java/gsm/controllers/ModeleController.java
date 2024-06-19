package gsm.controllers;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gsm.entities.Marque;
import gsm.entities.Modele;
import gsm.repositories.MarqueRepository;
import gsm.repositories.ModeleRepository;
import gsm.service.ModeleService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/modele")
public class ModeleController {
	@Autowired
	private ModeleRepository modeleRepository;
	@Autowired
	private ModeleService modeleService;
	@Autowired
	MarqueRepository marqueRepository;
	@GetMapping
	public List<Modele> getAllModeles(){
		return modeleService.getAllModeles();
	}
	@GetMapping("/by-marque/{marqueId}")
	public ResponseEntity<List<Modele>> getModelesByMarqueId(@PathVariable Long marqueId) {
	    List<Modele> modeles = modeleRepository.findByMarqueId(marqueId);
	    if (modeles.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(modeles);
	}

	@GetMapping(path="/{id}")
	public ResponseEntity<Modele> findModeleById(@PathVariable Long id){
		Modele modele = modeleService.findModeleById(id);
		if (modele == null) {
			return new ResponseEntity<Modele>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Modele>(modele, HttpStatus.OK);
		}
	}
	
	@PostMapping("/image")
	public Modele uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("nom") String nom, @RequestParam("marqueId") Long marqueId) {
	    try {
	        Marque marque = marqueRepository.findById(marqueId)
	                         .orElseThrow(() -> new RuntimeException("Marque not found!"));

	        Modele modele = new Modele();
	        modele.setNom(nom);
	        modele.setImage(file.getBytes());
	        modele.setMarque(marque);

	        return modeleRepository.save(modele);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to upload image!");
	    }
	}

	@PutMapping(path="/{id}")
	public ResponseEntity<Modele> updateModeleById(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("nom") String nom, @RequestParam("marqueId") Long marqueId) {
	    Optional<Modele> optionalModele = modeleRepository.findById(id);
	    if(optionalModele.isPresent()) {
	        try {
	            Modele existingModele = optionalModele.get();
	            existingModele.setNom(nom);
	            if (file != null && !file.isEmpty()) {
	                existingModele.setImage(file.getBytes());
	            }

	            Marque marque = marqueRepository.findById(marqueId)
	                            .orElseThrow(() -> new RuntimeException("Marque not found!"));
	            existingModele.setMarque(marque);

	            Modele updatedModeleEntity = modeleRepository.save(existingModele);
	            return new ResponseEntity<>(updatedModeleEntity, HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@GetMapping("/{id}/image")
	    public ResponseEntity<byte[]> getModeleImage(@PathVariable Long id) {
	        Modele modele = modeleService.findModeleById(id);
	        if (modele != null && modele.getImage() != null) {
	            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_JPEG).body(modele.getImage());
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	@DeleteMapping(path="/supprimer/{id}")
	public void deleteModele(@PathVariable Long id) {
		modeleService.deleteModele(id);
	}
}
