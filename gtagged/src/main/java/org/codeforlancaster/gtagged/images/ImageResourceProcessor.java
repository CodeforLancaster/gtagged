package org.codeforlancaster.gtagged.images;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Component
public class ImageResourceProcessor implements ResourceProcessor<ImageResource> {

    @Override
    public ImageResource process(ImageResource imageResource) {
        Link link = ControllerLinkBuilder
                    .linkTo(ControllerLinkBuilder.methodOn(ImageController.class)).slash(imageResource.getUri()).withSelfRel();
        imageResource.add(link);
        return imageResource;
    }

}
