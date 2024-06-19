package gsm.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gsm.entities.PieceOrder;

@Repository
public interface PieceOrderRepository extends JpaRepository<PieceOrder, Long> {
    List<PieceOrder> findByPhoneNumber(String phoneNumber);
}