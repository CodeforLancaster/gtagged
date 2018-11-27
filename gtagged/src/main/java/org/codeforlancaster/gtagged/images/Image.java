package org.codeforlancaster.gtagged.images;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Data @Entity
@Table(name = "images")
public class Image {

    double lat;
    double lon;
    String uri;
    String user;

    @CreatedDate
    LocalDateTime created;

}
