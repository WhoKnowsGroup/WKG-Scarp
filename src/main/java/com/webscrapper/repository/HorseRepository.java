package com.webscrapper.repository;

import com.webscrapper.domain.Horse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Horse entity.
 */
@SuppressWarnings("unused")
public interface HorseRepository extends JpaRepository<Horse,Long> {

}
