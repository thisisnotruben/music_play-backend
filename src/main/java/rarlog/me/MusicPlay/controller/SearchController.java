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
import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.dto.SearchResponseDto;
import rarlog.me.MusicPlay.service.SearchService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search for artist/album/song name with filters")
    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponseDto getSearch(
            @RequestParam("query") String queryValue,
            @RequestParam("filter") Optional<List<String>> filters)
            throws SolrServerException, IOException {

        return searchService.query(queryValue, filters);
    }

}
