package gsm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gsm.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	 User findByTelAndPassword(String tel, String password);
	 boolean existsByTel(String tel);
	    Optional<User> findByTel(String tel);

}
