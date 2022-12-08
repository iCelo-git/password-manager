package inz_proj_app.repository;

import inz_proj_app.model.NumberOfExpiredPasswords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NumberOfExpiredPasswordsRepository extends JpaRepository<NumberOfExpiredPasswords, Long > {

    List<NumberOfExpiredPasswords> findAll();
}
