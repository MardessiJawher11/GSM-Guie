package gsm.service;

import java.util.List;

import gsm.entities.Piece;

public interface PieceService {
	List<Piece> getAllPieces();
    Piece findPieceById(Long id);
    Piece createPiece(Piece piece);
    Piece updatePieceById(Long id, Piece updatedPiece);
    void deletePiece(Long id);
}

