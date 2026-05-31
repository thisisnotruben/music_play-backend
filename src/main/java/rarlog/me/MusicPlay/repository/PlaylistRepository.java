package rarlog.me.MusicPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rarlog.me.entity.AppUser;
import rarlog.me.entity.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findAllByAppUser(AppUser user);

}
