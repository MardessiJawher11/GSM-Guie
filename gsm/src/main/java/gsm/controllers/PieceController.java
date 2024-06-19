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

import gsm.entities.Modele;
import gsm.entities.Piece;
import gsm.repositories.ModeleRepository;
import gsm.repositories.PieceRepository;
import gsm.service.PieceService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/piece")
public class PieceController {
	@Autowired
	private PieceRepository pieceRepository;
	@Autowired
	private PieceService pieceService;
	@Autowired
	ModeleRepository modeleRepository;

	@GetMapping
	public List<Piece> getAllPieces(){
		return pieceService.getAllPieces();
	}

	@GetMapping("/by-modele/{modeleId}")
	public ResponseEntity<List<Piece>> getPiecesByModeleId(@PathVariable Long modeleId) {
	    List<Piece> pieces = pieceRepository.findByModeleId(modeleId);
	    if (pieces.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(pieces);
	}

	@GetMapping(path="/{id}")
	public ResponseEntity<Piece> findPieceById(@PathVariable Long id){
		Piece piece = pieceService.findPieceById(id);
		if (piece == null) {
			return new ResponseEntity<Piece>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Piece>(piece, HttpStatus.OK);
		}
	}

	@PostMapping("/image")
	public Piece uploadImage(
	        @RequestParam("file") MultipartFile file,
	        @RequestParam("nom") String nom,
	        @RequestParam("prix") Long prix, // Add prix parameter
	        @RequestParam("modeleId") Long modeleId) {
	    try {
	        Modele modele = modeleRepository.findById(modeleId)
	                .orElseThrow(() -> new RuntimeException("Modele not found!"));

	        Piece piece = new Piece();
	        piece.setNom(nom);
	        piece.setPrix(prix); // Set prix
	        piece.setImage(file.getBytes());
	        piece.setModele(modele);

	        return pieceRepository.save(piece);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to upload image!");
	    }
	}

	@PutMapping(path="/{id}")
	public ResponseEntity<Piece> updatePieceById(
	        @PathVariable Long id,
	        @RequestParam(value = "file", required = false) MultipartFile file,
	        @RequestParam("nom") String nom,
	        @RequestParam("prix") Long prix, 
	        @RequestParam("modeleId") Long modeleId) {
	    Optional<Piece> optionalPiece = pieceRepository.findById(id);
	    if(optionalPiece.isPresent()) {
	        try {
	            Piece existingPiece = optionalPiece.get();
	            existingPiece.setNom(nom);
	            existingPiece.setPrix(prix); 
	            if (file != null && !file.isEmpty()) {
	                existingPiece.setImage(file.getBytes());
	            }

	            Modele modele = modeleRepository.findById(modeleId)
	                    .orElseThrow(() -> new RuntimeException("Modele not found!"));
	            existingPiece.setModele(modele);

	            Piece updatedPieceEntity = pieceRepository.save(existingPiece);
	            return new ResponseEntity<>(updatedPieceEntity, HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}


	@GetMapping("/{id}/image")
	    public ResponseEntity<byte[]> getPieceImage(@PathVariable Long id) {
	        Piece piece = pieceService.findPieceById(id);
	        if (piece != null && piece.getImage() != null) {
	            return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_JPEG).body(piece.getImage());
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	@DeleteMapping(path="/supprimer/{id}")
	public void deletePiece(@PathVariable Long id) {
		pieceService.deletePiece(id);
	}}