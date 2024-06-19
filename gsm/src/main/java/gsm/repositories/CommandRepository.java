package gsm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.Command;
@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
}