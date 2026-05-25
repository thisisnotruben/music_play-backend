package rarlog.me.MusicPlay.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.exception.UserNotFoundException;
import rarlog.me.MusicPlay.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserPrincipalDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserPrincipal(appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)));
    }

}
