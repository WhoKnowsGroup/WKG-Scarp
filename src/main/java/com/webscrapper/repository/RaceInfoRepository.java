package com.webscrapper.repository;

import com.webscrapper.domain.RaceInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the RaceInfo entity.
 */
@SuppressWarnings("unused")
public interface RaceInfoRepository extends JpaRepository<RaceInfo,Long> {
	
	   @Query("Select s FROM RaceInfo s WHERE s.race_id=:id")
	   List<RaceInfo> findByRaceId(@Param("id") Long id);

	   @Query("Select s FROM RaceInfo s WHERE s.state=:state")
	   List<RaceInfo> findByState(@Param("state") String state);

	   @Query("Select s FROM RaceInfo s WHERE s.horseName=:horseName order by createdDate desc")
	   List<RaceInfo> findByLastTenRaces(@Param("horseName") String horseName);
	   
	   @Query("Select s FROM RaceInfo s WHERE s.horseName=:horseName order by createdDate desc")
	   List<RaceInfo> lastRaceDate(@Param("horseName") String horseName);
	   
}
