package com.seatreservation.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

import com.seatreservation.domain.enumeration.RestrictionType;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "restrictions")
public class Restrictions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "from_date", nullable = false)
    private ZonedDateTime fromDate;

    @Column(name = "to_date")
    private ZonedDateTime toDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "restriction_type", nullable = false)
    private RestrictionType restrictionType;

    @Range(min=1, max=100)
    @Column(name = "restriction_value")
    private Integer restrictionValue;

    public Long getId() {
        return this.id;
    }

    public Restrictions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Restrictions name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getFromDate() {
        return this.fromDate;
    }

    public Restrictions fromDate(ZonedDateTime fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return this.toDate;
    }

    public Restrictions toDate(ZonedDateTime toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public RestrictionType getRestrictionType() {
        return this.restrictionType;
    }

    public Restrictions restrictionType(RestrictionType restrictionType) {
        this.setRestrictionType(restrictionType);
        return this;
    }

    public void setRestrictionType(RestrictionType restrictionType) {
        this.restrictionType = restrictionType;
    }

    public Integer getRestrictionValue() {
        return this.restrictionValue;
    }

    public Restrictions restrictionValue(Integer restrictionValue) {
        this.setRestrictionValue(restrictionValue);
        return this;
    }

    public void setRestrictionValue(Integer restrictionValue) {
        this.restrictionValue = restrictionValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restrictions)) {
            return false;
        }
        return id != null && id.equals(((Restrictions) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Restrictions{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", restrictionType='" + getRestrictionType() + "'" +
            ", restrictionValue=" + getRestrictionValue() +
            "}";
    }
}
