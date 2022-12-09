package inz_proj_app.service.interfaces;

import inz_proj_app.dto.PasswordsDto;

import java.util.List;
import java.util.Set;

public interface PasswordsService {

    public List<PasswordsDto> findAllByUrl(String url);

    public List<PasswordsDto> findAllByEmail(String email);

    public List<PasswordsDto> findAll();

    public void saveNewPassword(PasswordsDto passwordsDto);

    public void saveNewPasswordDuplicate(PasswordsDto passwordsDto);

    public List<PasswordsDto> findPasswordsByUser();

    public void deletePassword(Long id);

    public void updatePassword(Long id, PasswordsDto passwordsDto);
    public PasswordsDto findPasswordById(Long id);

    public List<PasswordsDto> findAllByEmailOrUrl(String input);

    public List<PasswordsDto> findPasswordsSuggestedToBeChanged();

    public Integer findNumberOfEpiredPasswords();

    public List<PasswordsDto> checkIfThoseCreditentialsExists(PasswordsDto passwordsDto);

    public Set<PasswordsDto> loadDuplicatedPasswords();

}

