package inz_proj_app.exeptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotActive extends AuthenticationException {



    public UserNotActive(String msg) {
        super(String.format("User %s is not acticve. Check your email.", msg));
    }
}
