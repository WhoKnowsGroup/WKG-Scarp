package com.webscrapper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Horse.
 */
@Entity
@Table(name = "horse")
public class Horse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "horse_name")
    private String horseName;

    @Column(name = "horse_status")
    private String horseStatus;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "owner")
    private String owner;

    @Column(name = "stewards_embargoes")
    private String stewardsEmbargoes;

    @Column(name = "emergency_vaccination_record_url")
    private String emergencyVaccinationRecordURL;

    @Column(name = "last_gear_change")
    private LocalDate lastGearChange;

    @Column(name = "trainer")
    private String trainer;

    @Column(name = "prize", precision=10, scale=2)
    private BigDecimal prize;

    @Column(name = "bonus", precision=10, scale=2)
    private BigDecimal bonus;

    @Column(name = "mim_max_dist_win")
    private String mimMaxDistWin;

    @Column(name = "first_up_data")
    private String firstUpData;

    @Column(name = "second_up_data")
    private String secondUpData;

    @Column(name = "horse_track")
    private String horseTrack;

    @Column(name = "horse_dist")
    private String horseDist;

    @Column(name = "horse_class")
    private String horseClass;

    @Column(name = "position_in_last_race")
    private Integer positionInLastRace;

    @Column(name = "firm")
    private String firm;

    @Column(name = "good")
    private String good;

    @Column(name = "soft")
    private String soft;

    @Column(name = "heavy")
    private String heavy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHorseName() {
        return horseName;
    }

    public Horse horseName(String horseName) {
        this.horseName = horseName;
        return this;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getHorseStatus() {
        return horseStatus;
    }

    public Horse horseStatus(String horseStatus) {
        this.horseStatus = horseStatus;
        return this;
    }

    public void setHorseStatus(String horseStatus) {
        this.horseStatus = horseStatus;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Horse birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getOwner() {
        return owner;
    }

    public Horse owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStewardsEmbargoes() {
        return stewardsEmbargoes;
    }

    public Horse stewardsEmbargoes(String stewardsEmbargoes) {
        this.stewardsEmbargoes = stewardsEmbargoes;
        return this;
    }

    public void setStewardsEmbargoes(String stewardsEmbargoes) {
        this.stewardsEmbargoes = stewardsEmbargoes;
    }

    public String getEmergencyVaccinationRecordURL() {
        return emergencyVaccinationRecordURL;
    }

    public Horse emergencyVaccinationRecordURL(String emergencyVaccinationRecordURL) {
        this.emergencyVaccinationRecordURL = emergencyVaccinationRecordURL;
        return this;
    }

    public void setEmergencyVaccinationRecordURL(String emergencyVaccinationRecordURL) {
        this.emergencyVaccinationRecordURL = emergencyVaccinationRecordURL;
    }

    public LocalDate getLastGearChange() {
        return lastGearChange;
    }

    public Horse lastGearChange(LocalDate lastGearChange) {
        this.lastGearChange = lastGearChange;
        return this;
    }

    public void setLastGearChange(LocalDate lastGearChange) {
        this.lastGearChange = lastGearChange;
    }

    public String getTrainer() {
        return trainer;
    }

    public Horse trainer(String trainer) {
        this.trainer = trainer;
        return this;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public Horse prize(BigDecimal prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(BigDecimal prize) {
        this.prize = prize;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public Horse bonus(BigDecimal bonus) {
        this.bonus = bonus;
        return this;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getMimMaxDistWin() {
        return mimMaxDistWin;
    }

    public Horse mimMaxDistWin(String mimMaxDistWin) {
        this.mimMaxDistWin = mimMaxDistWin;
        return this;
    }

    public void setMimMaxDistWin(String mimMaxDistWin) {
        this.mimMaxDistWin = mimMaxDistWin;
    }

    public String getFirstUpData() {
        return firstUpData;
    }

    public Horse firstUpData(String firstUpData) {
        this.firstUpData = firstUpData;
        return this;
    }

    public void setFirstUpData(String firstUpData) {
        this.firstUpData = firstUpData;
    }

    public String getSecondUpData() {
        return secondUpData;
    }

    public Horse secondUpData(String secondUpData) {
        this.secondUpData = secondUpData;
        return this;
    }

    public void setSecondUpData(String secondUpData) {
        this.secondUpData = secondUpData;
    }

    public String getHorseTrack() {
        return horseTrack;
    }

    public Horse horseTrack(String horseTrack) {
        this.horseTrack = horseTrack;
        return this;
    }

    public void setHorseTrack(String horseTrack) {
        this.horseTrack = horseTrack;
    }

    public String getHorseDist() {
        return horseDist;
    }

    public Horse horseDist(String horseDist) {
        this.horseDist = horseDist;
        return this;
    }

    public void setHorseDist(String horseDist) {
        this.horseDist = horseDist;
    }

    public String getHorseClass() {
        return horseClass;
    }

    public Horse horseClass(String horseClass) {
        this.horseClass = horseClass;
        return this;
    }

    public void setHorseClass(String horseClass) {
        this.horseClass = horseClass;
    }

    public Integer getPositionInLastRace() {
        return positionInLastRace;
    }

    public Horse positionInLastRace(Integer positionInLastRace) {
        this.positionInLastRace = positionInLastRace;
        return this;
    }

    public void setPositionInLastRace(Integer positionInLastRace) {
        this.positionInLastRace = positionInLastRace;
    }

    public String getFirm() {
        return firm;
    }

    public Horse firm(String firm) {
        this.firm = firm;
        return this;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getGood() {
        return good;
    }

    public Horse good(String good) {
        this.good = good;
        return this;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getSoft() {
        return soft;
    }

    public Horse soft(String soft) {
        this.soft = soft;
        return this;
    }

    public void setSoft(String soft) {
        this.soft = soft;
    }

    public String getHeavy() {
        return heavy;
    }

    public Horse heavy(String heavy) {
        this.heavy = heavy;
        return this;
    }

    public void setHeavy(String heavy) {
        this.heavy = heavy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Horse horse = (Horse) o;
        if(horse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, horse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Horse{" +
            "id=" + id +
            ", horseName='" + horseName + "'" +
            ", horseStatus='" + horseStatus + "'" +
            ", birthDate='" + birthDate + "'" +
            ", owner='" + owner + "'" +
            ", stewardsEmbargoes='" + stewardsEmbargoes + "'" +
            ", emergencyVaccinationRecordURL='" + emergencyVaccinationRecordURL + "'" +
            ", lastGearChange='" + lastGearChange + "'" +
            ", trainer='" + trainer + "'" +
            ", prize='" + prize + "'" +
            ", bonus='" + bonus + "'" +
            ", mimMaxDistWin='" + mimMaxDistWin + "'" +
            ", firstUpData='" + firstUpData + "'" +
            ", secondUpData='" + secondUpData + "'" +
            ", horseTrack='" + horseTrack + "'" +
            ", horseDist='" + horseDist + "'" +
            ", horseClass='" + horseClass + "'" +
            ", positionInLastRace='" + positionInLastRace + "'" +
            ", firm='" + firm + "'" +
            ", good='" + good + "'" +
            ", soft='" + soft + "'" +
            ", heavy='" + heavy + "'" +
            '}';
    }
}
