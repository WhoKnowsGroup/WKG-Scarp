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
    
    @Column(name = "race_id")
    private Long race_id;
    
    @Column(name = "source")
    private String source;
    
    @Column(name = "last_margin_date")
    private LocalDate last_margin_date;
    
    @Column(name = "previous_margin")
    private String previous_margin;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "race_name")
    private String race_name;

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
    private String margin;

    @Column(name = "penalty")
    private String penalty;

    @Column(name = "starting_price")
    private String startingPrice;
    
    private String next_race_date;
    
    private String next_race_time;
    
    private String next_race_length;

	public String getNext_race_date() {
		return next_race_date;
	}

	public void setNext_race_date(String next_race_date) {
		this.next_race_date = next_race_date;
	}

	public String getNext_race_time() {
		return next_race_time;
	}

	public void setNext_race_time(String next_race_time) {
		this.next_race_time = next_race_time;
	}

	public String getNext_race_length() {
		return next_race_length;
	}

	public void setNext_race_length(String next_race_length) {
		this.next_race_length = next_race_length;
	}
    
    

    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPrevious_margin() {
		return previous_margin;
	}

	public void setPrevious_margin(String previous_margin) {
		this.previous_margin = previous_margin;
	}

	public String getRace_name() {
		return race_name;
	}

	public void setRace_name(String race_name) {
		this.race_name = race_name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public LocalDate getLast_margin_date() {
		return last_margin_date;
	}

	public void setLast_margin_date(LocalDate last_margin_date) {
		this.last_margin_date = last_margin_date;
	}

	public Long getRace_id() {
		return race_id;
	}

	public void setRace_id(Long race_id) {
		this.race_id = race_id;
	}
    
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

    public String getMargin() {
        return margin;
    }

    public RaceInfo margin(String margin) {
        this.margin = margin;
        return this;
    }

    public void setMargin(String margin) {
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
