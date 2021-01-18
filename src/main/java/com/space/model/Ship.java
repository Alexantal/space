package com.space.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;//название коробля (до 50 знаков)
    private String planet;//планета пребывания (до 50 знаков)
    @Enumerated(value = EnumType.STRING)
    private ShipType shipType;
    private Date prodDate;//дата выпуска(в диапазоне 2800...3019)
    private Boolean isUsed;//Использованный/новый
    private Double speed;//максимальная скорость корабля (0,01...0,99). Округления до сотых
    private Integer crewSize;//количество членов экипажа (1...9999)
    private Double rating;//рейтинг корабля. Окургление до сотых

    public Ship() {
    }

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize, Double rating) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Ship(Long id, String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDate.getTime());
        int prodYear = calendar.get(Calendar.YEAR);
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prodYear=" + prodYear +
                '}';
    }
}
