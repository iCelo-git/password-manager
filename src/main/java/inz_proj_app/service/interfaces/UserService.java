package inz_proj_app.service.interfaces;

import inz_proj_app.dto.UserRegistrationDto;
import inz_proj_app.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);

    void activateUser(String email, String token);
}
