package rarlog.me.MusicPlay.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import rarlog.me.Service.SearchService;
import rarlog.me.dto.AlbumDto;
import rarlog.me.dto.SearchResultDto;
import rarlog.me.dto.SongDto;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search for artist/album/song name with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(anyOf = {
                    SongDto.class, AlbumDto.class }))))
    })
    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SearchResultDto> getSearch(
            @RequestParam("query") String queryValue,
            @RequestParam("filter") Optional<List<String>> filters)
            throws SolrServerException, IOException {

        return searchService.query(queryValue, filters);
    }

}
