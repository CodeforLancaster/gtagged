package org.codeforlancaster.gtagged.images;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.codeforlancaster.gtagged.tags.Tag;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Data
@Entity
@Table(name = "images")
@EntityListeners(AuditingEntityListener.class)

@SqlResultSetMapping(name = "imageResultMapping",
        classes = { @ConstructorResult(targetClass = ImageResource.class,
                columns = { @ColumnResult(name = "uri", type = String.class),
                        @ColumnResult(name = "distance", type = Double.class),
                        @ColumnResult(name = "lat", type = Double.class),
                        @ColumnResult(name = "lon", type = Double.class) }) })
@NamedNativeQuery(name = "Image.search",
        query = "SELECT uri, distance, lat, lon\n"
                + "FROM (SELECT uri, ( 6371 * ACOS( COS( RADIANS(:lat) )  \n"
                + "          * COS( RADIANS( lat ) ) \n"
                + "          * COS( RADIANS( lon ) - RADIANS(:lon) ) + \n"
                + "             SIN( RADIANS(:lat) ) \n"
                + "          * SIN(RADIANS(lat)) ) ) distance, lat, lon \n"
                + "      FROM images)\n" + "WHERE distance < :radius \n"
                + "ORDER BY distance",
        resultSetMapping = "imageResultMapping")
public class Image {

    double lat;
    double lon;

    @Id
    String uri;
    String user;

    @CreatedDate
    LocalDateTime created;

    @ElementCollection
    Set<Tag> tags = new HashSet<Tag>();

    public Image tag(String value) {
        this.tags.add(new Tag(value));
        return this;
    }

}
