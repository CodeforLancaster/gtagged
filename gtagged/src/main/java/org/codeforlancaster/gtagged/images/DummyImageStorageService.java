package org.codeforlancaster.gtagged.images;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Service
public class DummyImageStorageService implements ImageStorageService {

    @Override
    public String store(MultipartFile file) {
        return UUID.randomUUID().toString();
    }

}
