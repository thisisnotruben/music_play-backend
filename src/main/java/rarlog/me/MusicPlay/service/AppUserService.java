package rarlog.me.MusicPlay.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.exception.AccountAlreadyExistsException;
import rarlog.me.MusicPlay.exception.UserNotFoundException;
import rarlog.me.MusicPlay.security.JWTService;
import rarlog.me.dto.AccountCreateDto;
import rarlog.me.dto.AppUserDto;
import rarlog.me.entity.AppUser;
import rarlog.me.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public void createAccount(AccountCreateDto accountCreateDto) {
        if (appUserRepository.existsByUsername(accountCreateDto.getUsername())) {
            throw new AccountAlreadyExistsException();
        }
        accountCreateDto.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
        appUserRepository.save(new AppUser(accountCreateDto));
    }

    public AppUserDto getAccountInfo(String username) {
        return new AppUserDto(getUser());
    }

    public String verify(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(username);
        }
        return "";
    }

    public void editUsername(String username, String newUsername) {
        AppUser user = getUser();
        user.setUsername(newUsername);
        appUserRepository.save(user);
    }

    public void editPassword(String username, String password) {
        AppUser user = getUser();
        user.setPassword(passwordEncoder.encode(password));
        appUserRepository.save(user);
    }

    public void editEmail(String username, String email) {
        AppUser user = getUser();
        user.setEmail(email);
        appUserRepository.save(user);
    }

    public void editFirstName(String username, String firstName) {
        AppUser user = getUser();
        user.setFirstName(firstName);
        appUserRepository.save(user);
    }

    public void editLastName(String username, String lastName) {
        AppUser user = getUser();
        user.setLastName(lastName);
        appUserRepository.save(user);
    }

    private AppUser getUser() {
        String username = "admin";
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

}
