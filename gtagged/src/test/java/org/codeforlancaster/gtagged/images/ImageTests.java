package org.codeforlancaster.gtagged.images;

import org.codeforlancaster.gtagged.AbstractMockMvcTest;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by wfaithfull on 27/11/18.
 */
public class ImageTests extends AbstractMockMvcTest {

    @Test
    public void testCreate() throws Exception {

        MockMultipartFile image = new MockMultipartFile("file", "graffiti.jpg", "image/jpeg", "nfoiaga".getBytes());
        MVC.perform(multipart("/images").file(image)
                .param("lat", "54.2083723")
                .param("lon", "-2.8978349")
        )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
    }

}
