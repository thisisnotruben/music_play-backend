package rarlog.me.MusicPlay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rarlog.me.MusicPlay.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
