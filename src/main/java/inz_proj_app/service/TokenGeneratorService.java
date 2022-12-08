package inz_proj_app.service;

import inz_proj_app.model.User;

public interface TokenGeneratorService {

    String generateToken();

    String findToken(User user);
}
