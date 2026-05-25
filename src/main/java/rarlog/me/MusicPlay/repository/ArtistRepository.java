package rarlog.me.MusicPlay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rarlog.me.MusicPlay.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
