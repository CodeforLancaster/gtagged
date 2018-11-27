package org.codeforlancaster.gtagged.images;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by wfaithfull on 27/11/18.
 */
@RepositoryRestResource(exported = false)
public interface ImageRepository extends PagingAndSortingRepository<Image, String> {

    @Query(nativeQuery = true)
    List<ImageResource> search(@Param("lat") double lat,
                               @Param("lon") double lon,
                               @Param("radius") double radius);

}
