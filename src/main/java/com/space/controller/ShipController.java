package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ShipController {
    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(value = "/rest/ships")
    public ResponseEntity<List<Ship>> read(@RequestParam(name = "name", defaultValue = "") String name,
        @RequestParam(name = "planet", defaultValue = "") String planet, @RequestParam(name = "shipType", required = false) ShipType shipType,
        @RequestParam(name = "after", defaultValue = "26192246400000") Long after, @RequestParam(name = "before", defaultValue = "33134745599000") Long before,
        @RequestParam(name = "isUsed", required = false) Boolean isUsed, @RequestParam(name = "minSpeed", defaultValue = "0.01D") Double minSpeed,
        @RequestParam(name = "maxSpeed", defaultValue = "0.99D") Double maxSpeed, @RequestParam(name = "minCrewSize", defaultValue = "1") Integer minCrewSize,
        @RequestParam(name = "maxCrewSize", defaultValue = "9999") Integer maxCrewSize, @RequestParam(name = "minRating", defaultValue = "0.00D") Double minRating,
        @RequestParam(name = "maxRating", defaultValue = "79.20D") Double maxRating,
        @RequestParam(name = "order", defaultValue = "ID") ShipOrder order, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) throws SQLException {

        final List<Ship> ships = shipService.getFilteredShipList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return ships != null && !ships.isEmpty()
                ? new ResponseEntity<>(ships, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/rest/ships/count")
    public ResponseEntity<Integer> readCount(@RequestParam(name = "name", defaultValue = "") String name,
        @RequestParam(name = "planet", defaultValue = "") String planet, @RequestParam(name = "shipType", required = false) ShipType shipType,
        @RequestParam(name = "after", defaultValue = "26192246400000") Long after, @RequestParam(name = "before", defaultValue = "33134745599000") Long before,
        @RequestParam(name = "isUsed", required = false) Boolean isUsed, @RequestParam(name = "minSpeed", defaultValue = "0.01D") Double minSpeed,
        @RequestParam(name = "maxSpeed", defaultValue = "0.99D") Double maxSpeed, @RequestParam(name = "minCrewSize", defaultValue = "1") Integer minCrewSize,
        @RequestParam(name = "maxCrewSize", defaultValue = "9999") Integer maxCrewSize, @RequestParam(name = "minRating", defaultValue = "0.00D") Double minRating,
        @RequestParam(name = "maxRating", defaultValue = "79.20D") Double maxRating) {
        final int count = shipService.getFilteredShipCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
        return count != 0
               ? new ResponseEntity<>(count, HttpStatus.OK)
               : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/rest/ships")
    public ResponseEntity<Ship> create(@RequestBody Ship ship) throws SQLException {
            final Ship result = shipService.create(ship);
            return result != null
                    ? new ResponseEntity<>(result, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/rest/ships/{id}")
    public ResponseEntity<Ship> readId(@PathVariable(name = "id") String id) {
        if (!id.matches("[+]?\\d+")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            long longId = Long.parseLong(id);
            if (longId == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            final Ship result = shipService.read(longId);

            return result != null
                    ? new ResponseEntity<>(result, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/rest/ships/{id}")
    public ResponseEntity<Ship> update(@PathVariable(name = "id") String id, @RequestBody Ship ship) throws SQLException {
        if (!id.matches("[+]?\\d+")) {//если не положительное цифровое вырожение вернуть код 400 bad request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            long longId = Long.parseLong(id);
            if (longId == 0) {//если id == 0 вернуть код 400 bad request
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            final Ship result;

            if (ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null && ship.getProdDate() == null
                && ship.getUsed() == null && ship.getSpeed() == null && ship.getCrewSize() == null && ship.getRating() == null) {//если пустое тело вернуть корабль с id и статус 200 OK
                result = shipService.read(longId);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            if (ship.getName() != null && (ship.getName().equals("") || ship.getName().length() > 50)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getPlanet() != null && (ship.getPlanet().equals("") || ship.getPlanet().length() > 50)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (ship.getProdDate() != null && (ship.getProdDate().getTime() < 26192246400000L || ship.getProdDate().getTime() > 33134745599000L)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getSpeed() != null && (ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (ship.getRating() != null && (ship.getRating() < 0 || ship.getRating() > 79.2D)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            result = shipService.update(ship, longId);
            return result != null
                    ? new ResponseEntity<>(result, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) throws SQLException {
        if (!id.matches("[+]?\\d+")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            long longId = Long.parseLong(id);
            if (longId == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            final boolean isDeleted = shipService.delete(longId);
            return isDeleted
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
