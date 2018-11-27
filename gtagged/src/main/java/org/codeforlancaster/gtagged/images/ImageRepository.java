package org.codeforlancaster.gtagged.images;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by wfaithfull on 27/11/18.
 */
@RepositoryRestResource(exported = false)
public interface ImageRepository extends PagingAndSortingRepository<Image, String> {

}
