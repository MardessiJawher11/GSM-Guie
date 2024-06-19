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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gsm.entities.Marque;
import gsm.repositories.MarqueRepository;
import gsm.service.MarqueService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/marque")
public class MarqueController {
	@Autowired
	private MarqueRepository marqueRepository;
	@Autowired
	private MarqueService marqueService;
	@GetMapping
	public List<Marque> getAllMarques(){
		return marqueService.getAllMarques();
	}
	@GetMapping(path="/{id}")
	public ResponseEntity<Marque>findMarqueById(@PathVariable Long id){
		Marque marque=marqueService.findMarqueById(id);
		if (marque==null) {
			return new ResponseEntity<Marque>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Marque>(marque,HttpStatus.OK);
		}
	}
	@PostMapping("/image")
    public Marque uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("nom") String nom) {
        try {
            Marque marque = new Marque();
            marque.setNom(nom);
            marque.setImage(file.getBytes());
            return marqueRepository.save(marque);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image!");
        }
    }
	@GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getMarqueImage(@PathVariable Long id) {
        Marque marque = marqueService.findMarqueById(id);
        if (marque != null && marque.getImage() != null) {
            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_JPEG).body(marque.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	@PutMapping(path="/{id}")
	public ResponseEntity<Marque>updateMarqueById(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("nom") String nom){
		Optional<Marque>optionalMarque = marqueRepository.findById(id);
		if(optionalMarque.isPresent()) {
			 try {
		            Marque existingMarque = optionalMarque.get();
		            existingMarque.setNom(nom);
		            if (file != null && !file.isEmpty()) {
		                existingMarque.setImage(file.getBytes());
		            }
		            Marque updatedMarqueEntity = marqueRepository.save(existingMarque);
		            return new ResponseEntity<>(updatedMarqueEntity, HttpStatus.OK);
		        } catch (IOException e) {
		            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    } else {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
		}
	@DeleteMapping(path="/supprimer/{id}")
	public void deleteMarque(@PathVariable Long id) {
		marqueService.deleteMarque(id);
	}
}
