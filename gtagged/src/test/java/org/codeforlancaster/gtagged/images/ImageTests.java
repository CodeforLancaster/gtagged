package org.codeforlancaster.gtagged.images;

import org.codeforlancaster.gtagged.AbstractMockMvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by wfaithfull on 27/11/18.
 */
public class ImageTests extends AbstractMockMvcTest {

    @MockBean
    ImageStorageService imageStorageService;

    @Autowired
    ImageController controllerUnderTest;

    @Test
    public void testCreate() throws Exception {

        MockMultipartFile image = new MockMultipartFile("file", "graffiti.jpg", "image/jpeg", "nfoiaga".getBytes());
        when(imageStorageService.store(image)).thenReturn("123");
                MVC.perform(multipart("/images").file(image)
                .param("lat", "54.2083723")
                .param("lon", "-2.8978349")
        ).andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "123"));
    }

    @Test
    public void testCreateWithTags() throws Exception {

        MockMultipartFile image = new MockMultipartFile("file", "graffiti.jpg", "image/jpeg", "nfoiaga".getBytes());
        when(imageStorageService.store(image)).thenReturn("123");
        MVC.perform(multipart("/images").file(image)
                .param("lat", "54.2083723")
                .param("lon", "-2.8978349")
                .param("tag", "#graffiti")
                .param("tag", "#art")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "123"));
    }

    @Test
    public void testSearch() throws Exception {

        MockMultipartFile image = new MockMultipartFile("file", "graffiti.jpg", "image/jpeg", "89y3rw8h93f93f".getBytes());
        when(imageStorageService.store(image)).thenReturn("456");

        // Alice posts a photo from the Borough
        MVC.perform(multipart("/images").file(image)
                .param("lat", "54.0481701")
                .param("lon", "-2.797949")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "456"));

        // Bob looks up local photos from HMP Lancaster Farms (Naughty Bob)
        MVC.perform(get("/images")
            .param("lat", "54.052990")
            .param("lon", "-2.771013")
            .param("radius", "10"))
        .andDo(print());
    }

}
