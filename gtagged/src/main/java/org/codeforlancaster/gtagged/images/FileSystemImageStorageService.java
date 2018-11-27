package org.codeforlancaster.gtagged.images;

import org.codeforlancaster.gtagged.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${gtagged.storage.directory}")
    private String directory;

    final MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");

    public FileSystemImageStorageService() throws NoSuchAlgorithmException { }

    @Override
    public String store(MultipartFile file) throws IOException {

        byte[] bytes = SHA256.digest(file.getBytes());
        String hash = Hex.toHexString(bytes);

        if(!Files.exists(Paths.get(directory))) {
            Files.createDirectories(Paths.get(directory));
        }

        Path path = Paths.get(directory, hash);

        Files.write(path.toAbsolutePath(), file.getBytes());

        return hash;
    }

    @Override
    public InputStream retrieve(String key) throws IOException {
        Path path = Paths.get(directory, key);
        return new FileInputStream(path.toFile());
    }

}
