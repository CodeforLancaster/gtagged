package org.codeforlancaster.gtagged.images;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wfaithfull on 27/11/18.
 */
public interface ImageStorageService {

    String store(MultipartFile file) throws IOException;

    InputStream retrieve(String key) throws IOException;

}
