package gsm.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.entities.Piece;
import gsm.repositories.PieceRepository;

@Service
public class PieceServiceImpl implements PieceService {

    @Autowired
    private PieceRepository pieceRepository;

    public List<Piece> getAllPieces() {
        return pieceRepository.findAll();
    }

    public Piece findPieceById(Long id) {
        return pieceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Piece not found with ID: " + id));
    }

    public Piece createPiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    public Piece updatePieceById(Long id, Piece updatedPiece) {
        Optional<Piece> pieceOptional = pieceRepository.findById(id);
        if (pieceOptional.isPresent()) {
            Piece existingPiece = pieceOptional.get();
            if (updatedPiece.getNom() != null) {
                existingPiece.setNom(updatedPiece.getNom());
            }
            return pieceRepository.save(existingPiece);
        } else {
            throw new NoSuchElementException("Piece not found with ID: " + id);
        }
    }

    public void deletePiece(Long id) {
        pieceRepository.deleteById(id);
    }



   
}



