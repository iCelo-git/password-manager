package inz_proj_app.service;

import inz_proj_app.model.User;
import inz_proj_app.service.interfaces.TokenGeneratorService;

import java.security.SecureRandom;

public class TokenGeneratorServiceImpl implements TokenGeneratorService {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe

    @Override
    public String generateToken() {
        byte bytes[] = new byte[20];
        secureRandom.nextBytes(bytes);
        return bytes.toString();
    }

    @Override
    public String findToken(User user) {
        return null;
    }
}
