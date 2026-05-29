package rarlog.me.MusicPlay.controller;

import java.io.IOException;

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

    @Operation(summary = "Search for song/abum name and return result")
    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponseDto getSearch(@RequestParam("queryName") String queryName)
            throws SolrServerException, IOException {
        return searchService.query(queryName);
    }

}
