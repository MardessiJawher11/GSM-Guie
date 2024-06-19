package gsm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.PieceItem;

@Repository
public interface PieceItemRepository extends JpaRepository<PieceItem, Long> {
    // You can add custom query methods here if needed
}