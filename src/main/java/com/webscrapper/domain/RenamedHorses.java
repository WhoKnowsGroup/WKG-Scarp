package com.webscrapper.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RenamedHorses.
 */
@Entity
@Table(name = "renamed_horses")
public class RenamedHorses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "old_name")
    private String oldName;

    @Column(name = "new_name")
    private String newName;

    @Column(name = "date_changed")
    private LocalDate dateChanged;

    @Column(name = "age_and_breed")
    private String ageAndBreed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldName() {
        return oldName;
    }

    public RenamedHorses oldName(String oldName) {
        this.oldName = oldName;
        return this;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public RenamedHorses newName(String newName) {
        this.newName = newName;
        return this;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public LocalDate getDateChanged() {
        return dateChanged;
    }

    public RenamedHorses dateChanged(LocalDate dateChanged) {
        this.dateChanged = dateChanged;
        return this;
    }

    public void setDateChanged(LocalDate dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getAgeAndBreed() {
        return ageAndBreed;
    }

    public RenamedHorses ageAndBreed(String ageAndBreed) {
        this.ageAndBreed = ageAndBreed;
        return this;
    }

    public void setAgeAndBreed(String ageAndBreed) {
        this.ageAndBreed = ageAndBreed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenamedHorses renamedHorses = (RenamedHorses) o;
        if(renamedHorses.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, renamedHorses.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RenamedHorses{" +
            "id=" + id +
            ", oldName='" + oldName + "'" +
            ", newName='" + newName + "'" +
            ", dateChanged='" + dateChanged + "'" +
            ", ageAndBreed='" + ageAndBreed + "'" +
            '}';
    }
}
