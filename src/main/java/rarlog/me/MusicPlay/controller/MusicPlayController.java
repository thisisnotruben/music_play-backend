package rarlog.me.MusicPlay.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.dto.AlbumDto;
import rarlog.me.MusicPlay.dto.AllMusicDto;
import rarlog.me.MusicPlay.dto.ErrorResponseDto;
import rarlog.me.MusicPlay.dto.ExploreEntryDto;
import rarlog.me.MusicPlay.dto.PlaylistDto;
import rarlog.me.MusicPlay.service.MusicPlayService;

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
    @PostMapping("/createPlaylist")
    public void createPlaylist(HttpServletRequest httpServletRequest,
            @RequestParam("playlistName") String playlistName) {
        musicPlayService.createPlaylist(getUsername(), playlistName);
    }

    @Operation(summary = "Delete a playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @DeleteMapping("/deletePlaylist")
    public void deletePlaylist(HttpServletRequest httpServletRequest,
            @RequestParam("playlistId") int playlistId) {
        musicPlayService.deletePlaylist(getUsername(), playlistId);
    }

    @Operation(summary = "Add song to a specific playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Song or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/addSongToPlaylist")
    public void addSongToPlaylist(HttpServletRequest httpServletRequest,
            @RequestParam("songId") int songId,
            @RequestParam("playlistId") int playlistId) {
        musicPlayService.addSongToPlaylist(songId, playlistId);
    }

    @Operation(summary = "Delete song from a specific playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Song or Playlist not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/deleteSongFromPlaylist")
    public void deleteSongFromPlaylist(HttpServletRequest httpServletRequest,
            @RequestParam("songId") int songId,
            @RequestParam("playlistId") int playlistId) {
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

    @Operation(summary = "Get all albums")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AlbumDto.class))))
    })
    @GetMapping("/getAlbums")
    public List<AlbumDto> getAlbums() {
        return musicPlayService.getAlbums();
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

    @Operation(summary = "Get all music of library entire")
    @GetMapping("/getAllMusic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AllMusicDto.class))))
    })
    public List<AllMusicDto> getAllMusic() {
        return musicPlayService.getAllMusic();
    }

    private String getUsername() {
        return "admin";
    }

}
