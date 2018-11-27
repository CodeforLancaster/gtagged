package org.codeforlancaster.gtagged.images;

import lombok.NonNull;
import lombok.Setter;
import org.codeforlancaster.gtagged.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Service
public class FileSystemImageStorageService implements ImageStorageService {

    @Setter
    @Value("${gtagged.storage.directory}")
    private String directory;

    final MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");

    public FileSystemImageStorageService() throws NoSuchAlgorithmException { }

    @Override
    public String store(@NonNull MultipartFile file) throws IOException {

        try (InputStream input = file.getInputStream()) {
            try {
                ImageIO.read(input).toString();
                // It's BMP, PNG, JPG or GIF.
            } catch (Exception e) {
                throw new InvalidImageTypeException();
            }
        }

        byte[] bytes = SHA256.digest(file.getBytes());
        String hash = Hex.toHexString(bytes);

        if(!Files.exists(Paths.get(directory))) {
            Files.createDirectories(Paths.get(directory));
        }

        Path path = Paths.get(directory, hash);

        if(Files.exists(path)) {
            throw new DuplicateImageException();
        }

        Files.write(path.toAbsolutePath(), file.getBytes());

        return hash;
    }

    @Override
    public InputStream retrieve(String key) throws IOException {
        Path path = Paths.get(directory, key);
        return new FileInputStream(path.toFile());
    }

    public void clear(String key) throws IOException {
        Path path = Paths.get(directory, key);

        if(!Hex.isHex(key) || !path.toAbsolutePath().startsWith(Paths.get(directory).toAbsolutePath())) {
            throw new IllegalKeyException();
        }

        path.toFile().delete();
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "The specified key is illegal.")
    public static class IllegalKeyException extends RuntimeException {}

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The provided file is not a valid image file.")
    public static class InvalidImageTypeException extends RuntimeException {}

    @ResponseStatus(code = HttpStatus.CONFLICT, reason = "This image is already in storage.")
    public static class DuplicateImageException extends RuntimeException {}
}
