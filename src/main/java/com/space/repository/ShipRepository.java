package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long>/*, ShipCustomRepository<Ship>*/ {
    //isUsed == null && shipType == null
    List<Ship> findAllByNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
            (String name, String planet, Date after, Date before, double misSpeed, double maxSpeed, int minCrewSize, int maxCrewSize, double minRating, double maxRating);

    //isUsed != null && shipType == null
    List<Ship> findAllByIsUsedAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
            (boolean isUsed, String name, String planet, Date after, Date before, double misSpeed, double maxSpeed, int minCrewSize, int maxCrewSize, double minRating, double maxRating);

    //isUsed == null && shipType != null
    List<Ship> findAllByShipTypeAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
    (ShipType shipType, String name, String planet, Date after, Date before, double misSpeed, double maxSpeed, int minCrewSize, int maxCrewSize, double minRating, double maxRating);

    //isUsed != null && shipType != null
    List<Ship> findAllByIsUsedAndShipTypeAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
    (boolean isUsed, ShipType shipType, String name, String planet, Date after, Date before, double misSpeed, double maxSpeed, int minCrewSize, int maxCrewSize, double minRating, double maxRating);

}
