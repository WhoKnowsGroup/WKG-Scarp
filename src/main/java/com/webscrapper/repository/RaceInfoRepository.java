package com.webscrapper.repository;

import com.webscrapper.domain.RaceInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RaceInfo entity.
 */
@SuppressWarnings("unused")
public interface RaceInfoRepository extends JpaRepository<RaceInfo,Long> {

}
