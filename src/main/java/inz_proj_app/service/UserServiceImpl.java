package inz_proj_app.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import inz_proj_app.dto.UserRegistrationDto;
import inz_proj_app.exeptions.UserNotActive;
import inz_proj_app.model.EmailDetails;
import inz_proj_app.model.Role;
import inz_proj_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import inz_proj_app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    @Autowired
    private EmailService emailService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(UserRegistrationDto registration) {

        String token = tokenGeneratorService.generateToken();
        sendEmail(registration, token);

        User user = new User();
        user.setHash(token);
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setActive(false);
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public void activateUser(String email, String token) {
        User user = userRepository.findByEmail(email);
        if (user.getHash().equals(token)) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } else if (!user.isActive()) {
            throw new UserNotActive(user.getEmail());
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }



    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    private void sendEmail(UserRegistrationDto userDto, String token){
        emailService.sendSimpleMail(EmailDetails.builder()
                .subject("TOKEN")
                .msgBody(token)
                .recipient(userDto.getEmail())
                .build());
    }
}