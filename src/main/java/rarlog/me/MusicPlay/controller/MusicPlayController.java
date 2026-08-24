package rarlog.me.MusicPlay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rarlog.me.MusicPlay.service.MusicPlayService;
import rarlog.me.MusicPlay.service.UserIdService;
import rarlog.me.dto.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MusicPlayController {

    private final MusicPlayService musicPlayService;
    private final UserIdService userIdService;

    @Operation(summary = "Create a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/createPlaylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaylistDto createPlaylist(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("playlistName") String playlistName,
            @RequestParam("file") Optional<MultipartFile> playlistCover) {
        return musicPlayService.createPlaylist(userIdService.getUserId(jwt), playlistName, playlistCover);
    }

    @Operation(summary = "Edits a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping(value = "/editPlaylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlaylistDto editPLaylist(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("playlistId") long playlistId,
            @RequestParam("newName") Optional<String> newName,
            @RequestParam("file") Optional<MultipartFile> playlistCover) {
        return musicPlayService.editPlaylist(userIdService.getUserId(jwt), playlistId, newName, playlistCover);
    }

    @Operation(summary = "Delete a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @DeleteMapping("/deletePlaylist")
    public void deletePlaylist(@AuthenticationPrincipal Jwt jwt, @RequestParam("playlistId") long playlistId) {
        musicPlayService.deletePlaylist(userIdService.getUserId(jwt), playlistId);
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
        musicPlayService.deleteSongFromPlaylist(songId, playlistId);
    }

    @Operation(summary = "Get all playlists from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = PlaylistDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/getPlaylists")
    public List<PlaylistDto> getPlaylists(@AuthenticationPrincipal Jwt jwt) {
        return musicPlayService.getPlaylists(userIdService.getUserId(jwt));
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

}
