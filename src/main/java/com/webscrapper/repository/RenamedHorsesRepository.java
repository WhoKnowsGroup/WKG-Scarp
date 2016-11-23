package com.webscrapper.repository;

import com.webscrapper.domain.RenamedHorses;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RenamedHorses entity.
 */
@SuppressWarnings("unused")
public interface RenamedHorsesRepository extends JpaRepository<RenamedHorses,Long> {

}
