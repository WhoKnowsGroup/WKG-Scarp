package com.webscrapper.repository;

import com.webscrapper.domain.Race;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Race entity.
 */
@SuppressWarnings("unused")
public interface RaceRepository extends JpaRepository<Race,Long> {

}
