package org.codeforlancaster.gtagged.images;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by wfaithfull on 27/11/18.
 */
@Data
@Entity
@Table(name = "images")
@EntityListeners(AuditingEntityListener.class)

@SqlResultSetMapping(
        name = "imageResultMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ImageResource.class,
                        columns = {
                                @ColumnResult(name = "uri", type = String.class),
                                @ColumnResult(name = "distance", type = Double.class),
                                @ColumnResult(name = "lat", type = Double.class),
                                @ColumnResult(name = "lon", type = Double.class)
                        }
                )
        }
)
@NamedNativeQuery(name = "Image.search", query = "SELECT uri, distance, lat, lon\n" +
        "From (Select uri, ( 6371 * acos( cos( radians(:lat) )  \n" +
        "          * cos( radians( lat ) ) \n" +
        "          * cos( radians( lon ) - radians(:lon) ) + \n" +
        "             sin( radians(:lat) ) \n" +
        "          * sin(radians(lat)) ) ) distance, lat, lon \n" +
        "      From images)\n" +
        "Where distance < :radius \n" +
        "ORDER BY distance", resultSetMapping = "imageResultMapping")
public class Image {

    double lat;
    double lon;

    @Id
    String uri;
    String user;

    @CreatedDate
    LocalDateTime created;

}
