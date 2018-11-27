package org.codeforlancaster.gtagged.images;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Data @Entity
@Table(name = "images")
@EntityListeners(AuditingEntityListener.class)
public class Image {

    double lat;
    double lon;

    @Id
    String uri;
    String user;

    @CreatedDate
    LocalDateTime created;

}
