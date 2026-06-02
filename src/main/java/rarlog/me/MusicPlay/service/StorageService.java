package rarlog.me.MusicPlay.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import okhttp3.HttpUrl;

@Service
public class StorageService {

    private final String bucketName;
    private final MinioClient client;

    public StorageService(String url, String accessKey,
            String secretKey, String regionName, String bucketName) {

        this.bucketName = bucketName;
        this.client = MinioClient.builder()
                .endpoint(HttpUrl.get(url))
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
