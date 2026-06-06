package rarlog.me.MusicPlay.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.service.MusicPlayService;
import rarlog.me.dto.AlbumDto;
import rarlog.me.dto.ArtistDto;
import rarlog.me.dto.ErrorResponseDto;
import rarlog.me.dto.ExploreEntryDto;
import rarlog.me.dto.PlaylistDto;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MusicPlayController {

    private final MusicPlayService musicPlayService;

    @Operation(summary = "Create a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/createPlaylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaylistDto createPlaylist(
            @RequestParam("playlistName") String playlistName,
            @RequestParam("file") Optional<MultipartFile> playlistCover) {
        return musicPlayService.createPlaylist(getUsername(), playlistName, playlistCover);
    }

    @Operation(summary = "Edits a playlist")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping(value = "/editPlaylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaylistDto editPLaylist(
            @RequestParam("playlistId") long playlistId,
            @RequestParam("newName") Optional<String> newName,
            @RequestParam("file") Optional<MultipartFile> playlistCover) {
        return musicPlayService.editPlaylist(getUsername(), playlistId, newName, playlistCover);
    }

    @Operation(summary = "Delete a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @DeleteMapping("/deletePlaylist")
    public void deletePlaylist(@RequestParam("playlistId") long playlistId) {
        musicPlayService.deletePlaylist(getUsername(), playlistId);
    }

    @Operation(summary = "Add song to playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Song or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/addSongToPlaylist")
    public void addSongToPlaylist(@RequestParam("songId") long songId, @RequestParam("playlistId") long playlistId) {
        musicPlayService.addSongToPlaylist(songId, playlistId);
    }

    @Operation(summary = "Delete song from playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Song or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/deleteSongFromPlaylist")
    public void deleteSongFromPlaylist(@RequestParam("songId") long songId,
            @RequestParam("playlistId") long playlistId) {
        musicPlayService.deleteSongfromPlaylist(songId, playlistId);
    }

    @Operation(summary = "Get all playlists from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = PlaylistDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/getPlaylists")
    public List<PlaylistDto> getPlaylists() {
        return musicPlayService.getPlaylists(getUsername());
    }

    @Operation(summary = "Get album")
    @GetMapping("/getAlbum")
    public AlbumDto getAlbum(@RequestParam("albumId") long albumId) {
        return musicPlayService.getAlbum(albumId);
    }

    @Operation(summary = "Get all albums")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AlbumDto.class))))
    })
    @GetMapping("/getAlbums")
    public List<AlbumDto> getAlbums() {
        return musicPlayService.getAlbums();
    }

    @Operation(summary = "Get all artists")
    @GetMapping("/getArtists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ArtistDto.class))))
    })
    public List<ArtistDto> getArtists() {
        return musicPlayService.getArtists();
    }

    @Operation(summary = "Get explore feed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ExploreEntryDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/getExploreFeed")
    public List<ExploreEntryDto> getExploreFeed() {
        return musicPlayService.getExploreFeed();
    }

    private String getUsername() {
        return "admin";
    }

}
