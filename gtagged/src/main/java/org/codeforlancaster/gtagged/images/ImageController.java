package org.codeforlancaster.gtagged.images;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @RequestMapping(method = RequestMethod.GET, produces = "application/hal+json")
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

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public void get(
            @PathVariable("id") String id,
            HttpServletResponse response
    ) throws IOException {
        InputStream stream = imageStorageService.retrieve(id);
        response.setContentLength(stream.available());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(stream, response.getOutputStream());
    }

}
