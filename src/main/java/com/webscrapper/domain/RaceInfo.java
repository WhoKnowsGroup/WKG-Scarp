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
 * A RaceInfo.
 */
@Entity
@Table(name = "race_info")
public class RaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "start_position")
    private Integer startPosition;

	@Column(name = "finish_position")
    private Integer finishPosition;

    @Column(name = "horse_name")
    private String horseName;

    @Column(name = "trainer")
    private String trainer;

    @Column(name = "jockey")
    private String jockey;

    @Column(name = "margin")
    private Long margin;

    @Column(name = "penalty")
    private String penalty;

    @Column(name = "starting_price")
    private String startingPrice;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public RaceInfo createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }


    public Integer getStartPosition() {
        return startPosition;
    }

    public RaceInfo startPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public RaceInfo finishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
        return this;
    }

    public void setFinishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public String getHorseName() {
        return horseName;
    }

    public RaceInfo horseName(String horseName) {
        this.horseName = horseName;
        return this;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getTrainer() {
        return trainer;
    }

    public RaceInfo trainer(String trainer) {
        this.trainer = trainer;
        return this;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getJockey() {
        return jockey;
    }

    public RaceInfo jockey(String jockey) {
        this.jockey = jockey;
        return this;
    }

    public void setJockey(String jockey) {
        this.jockey = jockey;
    }

    public Long getMargin() {
        return margin;
    }

    public RaceInfo margin(Long margin) {
        this.margin = margin;
        return this;
    }

    public void setMargin(Long margin) {
        this.margin = margin;
    }

    public String getPenalty() {
        return penalty;
    }

    public RaceInfo penalty(String penalty) {
        this.penalty = penalty;
        return this;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public RaceInfo startingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
        return this;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceInfo raceInfo = (RaceInfo) o;
        if(raceInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, raceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RaceInfo{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", startPosition='" + startPosition + "'" +
            ", finishPosition='" + finishPosition + "'" +
            ", horseName='" + horseName + "'" +
            ", trainer='" + trainer + "'" +
            ", jockey='" + jockey + "'" +
            ", margin='" + margin + "'" +
            ", penalty='" + penalty + "'" +
            ", startingPrice='" + startingPrice + "'" +
            '}';
    }
}
