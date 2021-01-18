package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShipServiceImp implements ShipService{
    private final ShipRepository shipRepository;

    @Autowired
    public ShipServiceImp(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> readAll(){
        return shipRepository.findAll();
    }

    @Override
    public Ship create(Ship ship){
        Long id = ship.getId();
        if (id != null) {
            Ship controlShip = read(id);
            if (controlShip != null) {
                return update(controlShip, id);
            }
        }

        String name = ship.getName();
        String planet = ship.getPlanet();
        ShipType shipType = ship.getShipType();
        Long prodDate = ship.getProdDate() != null ? ship.getProdDate().getTime() : null;
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        boolean isUsed = ship.getUsed() != null && ship.getUsed();

        //Проверяем исходные данные
        if (name == null || name.equals("") || name.length() > 50 || planet == null || planet.equals("") || planet.length() > 50 || shipType == null
                || prodDate == null || prodDate < 26192246400000L || prodDate > 33134745599000L || speed == null || speed < 0.01
                || speed > 0.99 || crewSize == null || crewSize < 1 || crewSize > 9999)
            return null;
        //Вычисляем райтинг корабля
        Double k = isUsed ? 0.5 : 1;
        int prodYear = new Date(prodDate).getYear() + 1900;
        double rating = (80 * speed * k)/(3019 - prodYear + 1);
        //Округляем до двух знаков после запятой
        rating = Math.round(rating * 100.0)/100.0;

        Ship newShip = new Ship(name, planet, shipType, new Date(prodDate), isUsed, speed, crewSize, rating);

        shipRepository.save(newShip);

        return newShip;
    }

    @Override
    public Ship update(Ship ship, long id) {
        Ship curShip = read(id);
        if (curShip == null) {//Проверяем существует ли корабль с переданным id
            return null;
        }

        String newName = curShip.getName();
        String newPlanet = curShip.getPlanet();
        ShipType newShipType = curShip.getShipType();
        Date newProdDate = curShip.getProdDate();
        Double newSpeed = curShip.getSpeed();
        Integer newCrewSize = curShip.getCrewSize();
        Boolean newIsUsed = curShip.getUsed();

        if (ship.getName() != null && !ship.getName().equals("") && ship.getName().length() <= 50) {
            newName = ship.getName();
        }
        if (ship.getPlanet() != null && !ship.getPlanet().equals("") && ship.getPlanet().length() <= 50) {
            newPlanet = ship.getPlanet();
        }

        if (ship.getShipType() != null) {
            newShipType = ship.getShipType();
        }
        if (ship.getProdDate() != null && ship.getProdDate().getTime() >= 26192246400000L && ship.getProdDate().getTime() <= 33134745599000L) {
            newProdDate = ship.getProdDate();
        }
        if (ship.getSpeed() != null && ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99) {
            newSpeed = ship.getSpeed();
        }
        if (ship.getCrewSize() != null && ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999) {
            newCrewSize = ship.getCrewSize();
        }
        if (ship.getUsed() != null) {
            newIsUsed = ship.getUsed();
        }
        //Вычисляем райтинг
        Double k = newIsUsed ? 0.5 : 1;
        int prodYear = newProdDate.getYear() + 1900;
        double newRating = (80 * newSpeed * k)/(3019 - prodYear + 1);
        //Округляем до двух знаков после запятой
        newRating = Math.round(newRating * 100.0)/100.0;

        Ship newShip = new Ship(id, newName, newPlanet, newShipType, newProdDate, newIsUsed, newSpeed, newCrewSize, newRating);

        shipRepository.save(newShip);

        return newShip;
    }

    @Override
    public boolean delete(Long id) {
        if (read(id) != null){
            shipRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Ship read(Long id) {
       /*List<Ship> ships = new ArrayList<>();
       Statement statement = connection.createStatement();
       ResultSet resultSet = statement.executeQuery("SELECT * FROM ship WHERE id = " + id);
       readSQLString(ships, resultSet);

       if (ships.size() > 0) {
           return ships.get(0);
       } else {
           return null;
       }*/
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ship> getFilteredShipList(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                                          Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating,
                                          ShipOrder order, Integer pageNumber, Integer pageSize) {
        List<Ship> result = new ArrayList<>();
        int startIndex = pageNumber * pageSize;
        int endIndex = pageSize * (pageNumber + 1) - 1;
        //получаем отфильтрованный список кораблей
        List<Ship> ships = getShips(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        //количество кораболей в списке
        int shipsCount = ships.size();
        //сортируем список согласно заданному порядку
        shipSort(ships, order);
        //формируем конечный список согласно заданной странице и количеству кораблей на странице
        if (shipsCount > 0) {
            for (int i = startIndex; i <= endIndex; i++) {
                if (i < shipsCount) {
                    result.add(ships.get(i));
                }
            }
        }
        return result;
    }

    @Override
    public int getFilteredShipCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                                    Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {

        List<Ship> ships = getShips(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return ships.size();
    }

    //пределы даты производства
    //Thu Jan 01 03:00:00 MSK 1970 = 0 ms - ноль отсчёта времени - это 00:00:00 по Гринвичу, но 03:00:00 по Москве
    //Sat Jan 01 03:00:00 MSK 2800 = 26192246400000 ms - время начала 2800 года для Москвы
    //Sat Jan 01 02:59:59 MSK 3020 = 33134745599000 ms - время конца 3019 года для Москвы

    private List<Ship> getShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        List<Ship> ships;
        if (name.equals("") && planet.equals("") && shipType == null && after == 26192246400000L && before == 33134745599000L && isUsed == null
                && minSpeed == 0.01 && maxSpeed == 0.99 && minCrewSize == 1 && maxCrewSize == 9999 && minRating == 0.0 && maxRating == 79.2) {
            ships = readAll();
        } else {
            if (isUsed == null && shipType == null) {
                ships = shipRepository.
                        findAllByNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
                        (name, planet, new Date(after), new Date(before), minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
            } else if (isUsed != null && shipType == null){
                ships = shipRepository.
                        findAllByIsUsedAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
                                (isUsed, name, planet, new Date(after), new Date(before), minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
            } else if (isUsed == null && shipType != null) {
                ships = shipRepository.findAllByShipTypeAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
                        (shipType, name, planet, new Date(after), new Date(before), minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
            } else {
                ships = shipRepository.findAllByIsUsedAndShipTypeAndNameContainsAndPlanetContainsAndProdDateGreaterThanEqualAndProdDateIsLessThanEqualAndSpeedGreaterThanEqualAndSpeedIsLessThanEqualAndCrewSizeGreaterThanEqualAndAndCrewSizeLessThanEqualAndRatingGreaterThanEqualAndRatingLessThanEqual
                        (isUsed, shipType, name, planet, new Date(after), new Date(before), minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
            }
        }
        return ships;
    }

    private void shipSort(List<Ship> ships, ShipOrder order) {
        ships.sort((o1, o2) -> {
            switch (order) {
                case SPEED: {
                   return o1.getSpeed().compareTo(o2.getSpeed());
                }
                case DATE: {
                    return o1.getProdDate().compareTo(o2.getProdDate());
                }
                case RATING: {
                    return o1.getRating().compareTo(o2.getRating());
                }
                default: {
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });
    }

}
