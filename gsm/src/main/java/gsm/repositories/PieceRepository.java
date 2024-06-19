package gsm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gsm.entities.Piece;
@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {
    @Query("SELECT p FROM Piece p WHERE p.modele.id = :modeleId")
    List<Piece> findByModeleId(@Param("modeleId") Long modeleId);
}

