package org.codeforlancaster.gtagged.images;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wfaithfull on 27/11/18.
 */
public interface ImageStorageService {

    String store(MultipartFile file);

}
