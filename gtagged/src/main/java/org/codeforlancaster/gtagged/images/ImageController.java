package org.codeforlancaster.gtagged.images;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Controller
@RequestMapping("/images")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon) throws IOException {

        String uri = imageStorageService.store(file);
        Image image = new Image();
        image.setLat(lat);
        image.setLon(lon);
        image.setUri(uri);
        image.setUser("test");

        imageRepository.save(image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", uri)
                .build();
    }

}
