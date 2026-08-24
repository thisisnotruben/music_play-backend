package rarlog.me.MusicPlay.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserIdService {

    public String getUserId(Jwt jwt) {
        return jwt.getSubject();
    }

}
