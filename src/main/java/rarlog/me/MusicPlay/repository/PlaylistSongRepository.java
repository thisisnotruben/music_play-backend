package rarlog.me.MusicPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rarlog.me.MusicPlay.entity.PlaylistSong;
import rarlog.me.MusicPlay.entity.PlaylistSongKey;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongKey> {

    List<PlaylistSong> findAllByPlaylistId(long id);

}
