package org.codeforlancaster.gtagged.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Relation(value = "image", collectionRelation = "images")
@Data @EqualsAndHashCode(of = "uri") @AllArgsConstructor @NoArgsConstructor
public class ImageResource extends ResourceSupport {

    String uri;
    double distance;
    double lat;
    double lon;

}
