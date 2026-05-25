package rarlog.me.MusicPlay.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import rarlog.me.MusicPlay.dto.AccountCreateDto;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @OneToMany(mappedBy = "appUser")
    private List<Playlist> playlists;

    public AppUser(AccountCreateDto accountCreateDto) {
        this.username = accountCreateDto.getUsername();
        this.password = accountCreateDto.getPassword();
        this.firstName = accountCreateDto.getFirstName();
        this.lastName = accountCreateDto.getLastName();
        this.email = accountCreateDto.getEmail();
    }

}
