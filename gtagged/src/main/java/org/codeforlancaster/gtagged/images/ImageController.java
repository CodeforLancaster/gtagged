package org.codeforlancaster.gtagged.images;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Controller
@RequestMapping("/images")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {

    private static final double MAX_RADIUS_KM = 10;

    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam(value = "tags", required = false) List<String> tags) throws IOException {

        String uri = imageStorageService.store(file);
        Image image = new Image();
        image.setLat(lat);
        image.setLon(lon);
        image.setUri(uri);
        image.setUser("test");

        if(tags != null && !tags.isEmpty()) {
            tags.forEach(image::tag);
        }

        imageRepository.save(image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", uri)
                .build();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Resources<ImageResource> search(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("radius") double radius,
            HttpServletRequest request
    ) {
        if(radius > MAX_RADIUS_KM) {
            throw new IllegalStateException(String.format("Radius must be less than %.2fkm", MAX_RADIUS_KM));
        }

        List<ImageResource> images = imageRepository.search(lat, lon, radius);
        Link selfLink = new Link(request.getRequestURL().toString() + String.format("?lat=%f&lon=%f&radius=%f", lat, lon, radius)).withSelfRel();

        return new Resources<>(images, selfLink);
    }

}
