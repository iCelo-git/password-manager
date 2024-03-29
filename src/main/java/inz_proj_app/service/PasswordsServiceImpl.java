package inz_proj_app.service;

import inz_proj_app.dto.PasswordsDto;
import inz_proj_app.model.EmailDetails;
import inz_proj_app.model.NumberOfExpiredPasswords;
import inz_proj_app.model.Passwords;
import inz_proj_app.model.User;
import inz_proj_app.repository.NumberOfExpiredPasswordsRepository;
import inz_proj_app.repository.PasswordsRepository;
import inz_proj_app.repository.UserRepository;
import inz_proj_app.service.interfaces.EmailService;
import inz_proj_app.service.interfaces.PasswordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PasswordsServiceImpl implements PasswordsService {

    @Autowired
    PasswordsRepository passwordsRepository;

    @Autowired
    UserRepository repository;

    @Autowired
    NumberOfExpiredPasswordsRepository expiredPasswordsRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<PasswordsDto> findAllByUrl(String url) {
        return passwordsRepository.findAllByUrlIsContainingIgnoreCase(url)
                .stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }

    @Override
    public List<PasswordsDto> findAllByEmail(String email) {
        return passwordsRepository.findAllByEmail(email)
                .stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }

    @Override
    public List<PasswordsDto> findAll() {
        return passwordsRepository.findAll()
                .stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }



    @Override
    public List<PasswordsDto> findPasswordsByUser() {
        User user = getCurrentUser();
        checkIfPasswordsExpiered();
        return passwordsRepository.findPasswordsByUser(user)
                .stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePassword(Long id) {
        passwordsRepository.deleteById(id);
    }

    @Override
    public void updatePassword(Long id, PasswordsDto passwordsDto) {
        Optional<Passwords> passwords = passwordsRepository.findById(id);
        if (passwords.isPresent()){
            passwords.get().setPasswordHash(passwordsDto.getPasswordHash());
            passwords.get().setEmail(passwordsDto.getEmail());
            passwords.get().setUrl(passwordsDto.getUrl());
            passwords.get().setLastChange(LocalDateTime.now());
            Passwords pass = passwords.get();
            passwordsRepository.save(passwords.get());
        }

    }

    @Override
    public PasswordsDto findPasswordById(Long id) {
        return passwordsRepository.findById(id)
                .stream()
                .findFirst()
                .map(this::loadPassword)
                .get();
    }

    @Override
    public List<PasswordsDto> findAllByEmailOrUrl(String input) {
        return passwordsRepository.findAllByUrlIsContainingIgnoreCaseAndUserEquals(input, getCurrentUser())
                .stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }

    @Override
    public List<PasswordsDto> findPasswordsSuggestedToBeChanged() {
        User user = getCurrentUser();
        return passwordsRepository.findPasswordsByUser(user)
                .stream()
                .map(this::loadPassword)
                .filter(pass -> checkIfPasswordShouldBeChanged(pass.getLastChange()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer findNumberOfEpiredPasswords() {
        User user = getCurrentUser();
        if (user.getId() != null) {
            List<NumberOfExpiredPasswords> all = expiredPasswordsRepository.findAllByUserID(user.getId());
            if (!all.isEmpty()) {
                return all.stream().findFirst().get().getCounter();
            }
        }
        return 0;
    }

    @Override
    public List<PasswordsDto> checkIfThoseCreditentialsExists(PasswordsDto passwordsDto) {
        User user = getCurrentUser();
        List<Passwords> passwordsByEmailAndUrl = passwordsRepository.findPasswordsByEmailAndUrlAndUserEquals(passwordsDto.getEmail(), passwordsDto.getUrl(), user);
        if (passwordsByEmailAndUrl.isEmpty()) return null;

        return passwordsByEmailAndUrl.stream()
                .map(this::loadPassword)
                .collect(Collectors.toList());
    }

    @Override
    public Set<PasswordsDto> loadDuplicatedPasswords() {
        User user = getCurrentUser();
        List<Passwords> passwordsByDuplicate = passwordsRepository.findPasswordsByDuplicate(true);
        Set<Passwords> set = new HashSet<>();
        for (Passwords p :passwordsByDuplicate) {
            set.addAll(passwordsRepository.findPasswordsByEmailAndUrlAndUserEquals(p.getEmail(), p.getUrl(), user));
        }
        return set.stream()
                .map(this::loadPassword)
                .collect(Collectors.toSet());
    }

    @Override
    public String generateStrongPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        return passwordGenerator.generate(24);
    }

    @Override
    public void saveNewPassword(PasswordsDto passwordsDto) {
            Passwords passwords = new Passwords();
            passwords.setUrl(passwordsDto.getUrl());
            passwords.setPasswordHash(passwordsDto.getPasswordHash());
            passwords.setEmail((passwordsDto.getEmail()));
            passwords.setLastChange(LocalDateTime.now());
            passwords.setUser(getCurrentUser());
            passwords.setDuplicate(false);
            passwordsRepository.save(passwords);
    }

    @Override
    public void saveNewPasswordDuplicate(PasswordsDto passwordsDto) {
        Passwords passwords = new Passwords();
        passwords.setUrl(passwordsDto.getUrl());
        passwords.setPasswordHash(passwordsDto.getPasswordHash());
        passwords.setEmail((passwordsDto.getEmail()));
        passwords.setLastChange(LocalDateTime.now());
        passwords.setUser(getCurrentUser());
        passwords.setDuplicate(true);
        passwordsRepository.save(passwords);
    }

    private void checkIfPasswordsExpiered(){
        User user = getCurrentUser();
        List<PasswordsDto> listOfPasswords = passwordsRepository.findPasswordsByUser(user)
                .stream()
                .map(this::loadPassword)
                .filter(pass -> checkIfPasswordShouldBeChanged(pass.getLastChange()))
                .collect(Collectors.toList());

        if (!listOfPasswords.isEmpty() && !checkIfExpiredPasswordsNeedToBeUpdated(listOfPasswords)){
            expiredPasswordsRepository.deleteAll();
            expiredPasswordsRepository.save(new NumberOfExpiredPasswords(listOfPasswords.size(), user.getId()));
            sendEmail(listOfPasswords);
        }
    }

    private PasswordsDto loadPassword(Passwords password) {
        PasswordsDto passwordsDto = new PasswordsDto();
        passwordsDto.setId(password.getId());
        passwordsDto.setEmail(password.getEmail());
        passwordsDto.setPasswordHash(password.getPasswordHash());
        passwordsDto.setUrl(password.getUrl());
        passwordsDto.setLastChange(password.getLastChange());

        return passwordsDto;
    }

    private Passwords createPasswordsFromDto(PasswordsDto dto){
        Passwords passwords = new Passwords();
        passwords.setEmail(dto.getEmail());
        passwords.setLastChange(LocalDateTime.now());
        passwords.setPasswordHash(dto.getPasswordHash());
        passwords.setUrl(dto.getUrl());
        return passwords;
    }

    private User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();

            user = repository.findByEmail(username);
            return user;
        }else return null;
    }

    private boolean checkIfPasswordShouldBeChanged(LocalDateTime localDateTime){
        return ChronoUnit.MONTHS.between(localDateTime.toLocalDate(), LocalDate.now()) > 3;
    }

    private boolean checkIfExpiredPasswordsNeedToBeUpdated( List<PasswordsDto> listOfPasswords){
        return Objects.equals(listOfPasswords.size(), findNumberOfEpiredPasswords());
    }

    private void sendEmail(List<PasswordsDto> listOfPasswords){

        String str = listOfPasswords.stream()
                .map(PasswordsDto::getUrl)
                .collect(Collectors.joining(";"));

        emailService.sendSimpleMail(EmailDetails.builder()
                .subject("Passwords Expired")
                .msgBody(str)
                .recipient(getCurrentUser().getEmail())
                .build());
    }

}

