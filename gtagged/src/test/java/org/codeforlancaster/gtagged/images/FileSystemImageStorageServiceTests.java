package org.codeforlancaster.gtagged.images;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wfaithfull on 27/11/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileSystemImageStorageServiceTests {

    private static final FileSystemImageStorageService SERVICE;

    static {
        try {
            SERVICE = new FileSystemImageStorageService();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        SERVICE.setDirectory("/tmp/storage");
    }

    private static InputStream getFile(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

    @Test
    public void testStore() throws IOException {
        MultipartFile mpf = new MockMultipartFile("lancaster.jpg", "lancaster.jpg", "image/jpeg",
                getFile("lancaster.jpg"));
        String uri = SERVICE.store(mpf);
        SERVICE.clear(uri);
    }

    @Test(expected = FileSystemImageStorageService.InvalidImageTypeException.class)
    public void testStoreNonImage_ShouldFail() throws IOException {
        MultipartFile mpf = new MockMultipartFile("lancaster.jpg", "lancaster.jpg", "image/jpeg",
                "ioahwfioahfoiwahf".getBytes());
        String uri = SERVICE.store(mpf);
        SERVICE.clear(uri);
    }

    @Test
    public void testRetrieve() throws IOException {
        MultipartFile mpf = new MockMultipartFile("lancaster.jpg", "lancaster.jpg", "image/jpeg",
                getFile("lancaster.jpg"));
        String uri = SERVICE.store(mpf);

        MultipartFile mpf2 = new MockMultipartFile("lancaster.jpg", "lancaster.jpg", "image/jpeg",
                SERVICE.retrieve(uri));

        Assert.assertArrayEquals(mpf.getBytes(), mpf2.getBytes());

        SERVICE.clear(uri);
    }

    @Test
    public void testDuplicateImages_ShouldFail() throws IOException {
        MultipartFile mpf = new MockMultipartFile("lancaster.jpg", "lancaster.jpg", "image/jpeg",
                getFile("lancaster.jpg"));
        String uri = SERVICE.store(mpf);

        DuplicateKeyException ex = null;

        try {
            SERVICE.store(mpf);
        } catch (DuplicateKeyException e) {
            ex = e;
        }

        Assert.assertNotNull(ex);
        SERVICE.clear(uri);
    }

    @Test(expected = FileSystemImageStorageService.IllegalKeyException.class)
    public void testNaughtyDelete_ShouldFail() throws IOException {

        final String SECRET = "/tmp/super_secret_data";
        final String FILE = "encryption_keys.txt";
        if(!Files.exists(Paths.get(SECRET))) {
            Files.createDirectories(Paths.get(SECRET));
        }
        if(!Files.exists(Paths.get(SECRET, FILE))) {
            Files.createFile(Paths.get(SECRET, FILE));
        }

        SERVICE.clear(Paths.get(SECRET, FILE).toString());
    }

}
