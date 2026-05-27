package rarlog.me.MusicPlay.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.log4j.Log4j2;
import okhttp3.HttpUrl;

@Log4j2
@Service
public class StorageService {

    private final String bucketName;
    private final MinioClient client;

    public StorageService(
            @Value("${storage.accessKey}") String accessKey,
            @Value("${storage.secretKey}") String secretKey,
            @Value("${storage.regionName}") String regionName,
            @Value("${storage.bucketName}") String bucketName,
            @Value("${storage.host}") String host) {

        this.bucketName = bucketName;
        this.client = MinioClient.builder()
                .endpoint(HttpUrl.get(host))
                .region(regionName)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String uploadPlaylistCover(long userId, long playlistId, MultipartFile file) {
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream())) {

            String uploadedFilePath = String.format("%d/%d/cover.%s", userId, playlistId,
                    file.getContentType().split("/")[1]);

            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uploadedFilePath)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1L)
                    .build());

            return uploadedFilePath;

        } catch (IOException e) {
        } catch (MinioException e) {
        }
        return "";
    }

    public void deletePlaylistCover(String coverPath) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(coverPath)
                    .build());
        } catch (MinioException e) {
        }
    }

}
