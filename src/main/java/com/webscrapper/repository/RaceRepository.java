package com.webscrapper.repository;

import com.webscrapper.domain.Race;
import com.webscrapper.domain.RaceInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Race entity.
 */
@SuppressWarnings("unused")
public interface RaceRepository extends JpaRepository<Race,Long> {

	 @Query("Select s FROM Race s WHERE s.city=:city")
	   List<Race> findByState(@Param("city") String state);

}
