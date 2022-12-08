package inz_proj_app.service.interfaces;

import inz_proj_app.model.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}
