package gsm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    // You can add custom query methods here if needed
}