package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.sql.SQLException;
import java.util.List;

public interface ShipService {
    //возвращает список всех имеющихся кораблей
    List<Ship> readAll();

    //создание нового корабля
    Ship create(Ship ship);

    //обновляет клиента с заданным id
    Ship update(Ship ship, long id);

    //удаляет клиента с заданным id
    boolean delete(Long id);

    //возвращает корабль по его id
    Ship read(Long id);

    //возвращает список кораблей в соответствии с переданными фильтрами
    List<Ship> getFilteredShipList(String name, String planet, ShipType shipType, Long after, Long before,
                                   Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                   Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                                   Integer pageNumber, Integer pageSize);

    //возвращает количество кораблей, которые соответ переданным фильтрам
    int getFilteredShipCount(String name, String planet, ShipType shipType, Long after, Long before,
                             Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                             Integer maxCrewSize, Double minRating, Double maxRating);
}
