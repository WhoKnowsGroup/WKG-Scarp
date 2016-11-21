package com.webscrapper.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Race.
 */
@Entity
@Table(name = "race")
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "race_name")
    private String raceName;

    @Column(name = "city")
    private String city;

    @Column(name = "race_description")
    private String raceDescription;

    @Column(name = "state")
    private String state;

    @Column(name = "race_date")
    private LocalDate raceDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaceName() {
        return raceName;
    }

    public Race raceName(String raceName) {
        this.raceName = raceName;
        return this;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getCity() {
        return city;
    }

    public Race city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRaceDescription() {
        return raceDescription;
    }

    public Race raceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
        return this;
    }

    public void setRaceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
    }

    public String getState() {
        return state;
    }

    public Race state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public Race raceDate(LocalDate raceDate) {
        this.raceDate = raceDate;
        return this;
    }

    public void setRaceDate(LocalDate raceDate) {
        this.raceDate = raceDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Race race = (Race) o;
        if(race.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, race.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Race{" +
            "id=" + id +
            ", raceName='" + raceName + "'" +
            ", city='" + city + "'" +
            ", raceDescription='" + raceDescription + "'" +
            ", state='" + state + "'" +
            ", raceDate='" + raceDate + "'" +
            '}';
    }
}
