package rarlog.me.MusicPlay.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import rarlog.me.MusicPlay.dto.AlbumDto;
import rarlog.me.MusicPlay.dto.ArtistDto;
import rarlog.me.MusicPlay.dto.SearchDto;
import rarlog.me.MusicPlay.dto.SearchResponseDto;
import rarlog.me.MusicPlay.dto.SongDto;
import rarlog.me.MusicPlay.repository.AlbumRepository;
import rarlog.me.MusicPlay.repository.ArtistRepository;
import rarlog.me.MusicPlay.repository.SongRepository;

@Service
public class SearchService {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final HttpJdkSolrClient client;

    public SearchService(
            @Value("${Solr.url}") String solrUrl,
            @Value("${Solr.core}") String solrCore,
            ArtistRepository artistRepository,
            AlbumRepository albumRepository,
            SongRepository songRepository) {

        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;

        this.client = new HttpJdkSolrClient.Builder()
                .withBaseSolrUrl(solrUrl)
                .withDefaultCollection(solrCore)
                .build();
    }

    public void indexDatabase() throws SolrServerException, IOException {

        List<SearchDto> searchDtos = new ArrayList<>();
        artistRepository.findAll().stream().map(a -> new SearchDto(a)).forEach(searchDtos::add);
        albumRepository.findAll().stream().map(a -> new SearchDto(a)).forEach(searchDtos::add);
        songRepository.findAll().stream().map(s -> new SearchDto(s)).forEach(searchDtos::add);

        if (!searchDtos.isEmpty()) {
            client.addBeans(searchDtos);
            client.commit();
        }
    }

    public SearchResponseDto query(String queryValue, Optional<List<String>> optFilters)
            throws SolrServerException, IOException {

        SolrQuery solrQuery = new SolrQuery(
                String.join(", ", List.of("%s", "%s~").stream()
                        .map(q -> String.format(q, queryValue.trim()))
                        .collect(Collectors.toList())));

        solrQuery.add(CommonParams.DF, "name");
        solrQuery.add(CommonParams.INDENT, CommonParams.FALSE);
        solrQuery.add(CommonParams.ROWS, String.valueOf(10));
        solrQuery.add("q.op", "OR");
        solrQuery.add("useParams", "");

        if (optFilters.isPresent()) {
            final List<String> validFilters = List.of(SearchDto.TYPE_ARTIST, SearchDto.TYPE_ALBUM, SearchDto.TYPE_SONG);
            final List<String> filters = optFilters.get().stream()
                    .map(s -> s.trim().toUpperCase())
                    .filter(validFilters::contains)
                    .map(s -> "type:".concat(s))
                    .collect(Collectors.toList());

            if (filters.size() < validFilters.size()) {
                filters.stream().forEach(solrQuery::addFilterQuery);
            }
        }

        QueryResponse queryResponse = client.query(solrQuery);

        List<Long> artistIds = new ArrayList<>();
        List<Long> albumIds = new ArrayList<>();
        List<Long> songIds = new ArrayList<>();

        queryResponse.getBeans(SearchDto.class).stream().forEach(s -> {
            switch (s.getType()) {
                case SearchDto.TYPE_ARTIST:
                    artistIds.add(s.getDbId());
                    break;
                case SearchDto.TYPE_ALBUM:
                    albumIds.add(s.getDbId());
                    break;
                case SearchDto.TYPE_SONG:
                    songIds.add(s.getDbId());
                    break;
            }
        });

        return SearchResponseDto.builder()
                .artists(artistRepository.findAllById(artistIds).stream().map(a -> new ArtistDto(a))
                        .collect(Collectors.toList()))
                .albums(albumRepository.findAllById(albumIds).stream().map(a -> new AlbumDto(a))
                        .collect(Collectors.toList()))
                .songs(songRepository.findAllById(songIds).stream().map(s -> new SongDto(s))
                        .collect(Collectors.toList()))
                .build();
    }

    @PreDestroy
    private void cleanup() throws IOException {
        client.close();
    }

}
