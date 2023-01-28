package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot newParkingLot= new ParkingLot(name,address);
        parkingLotRepository1.save(newParkingLot);
        return newParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot newSpot= new Spot(numberOfWheels,pricePerHour);

        ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();
        newSpot.setParkingLot(parkingLot);
//        spotRepository1.save(newSpot);

        List<Spot> spotList= parkingLot.getSpotList();
        spotList.add(newSpot);
        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);
        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
//        Spot spot= spotRepository1.findById(spotId).get();
//
//        ParkingLot parkingLot= spot.getParkingLot();
//        List<Spot> spotList= parkingLot.getSpotList();
//        spotList.remove(spot);
//        parkingLot.setSpotList(spotList);
//        parkingLotRepository1.save(parkingLot);
//
//        spotRepository1.delete(spot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot updatedSpot= spotRepository1.findById(spotId).get();
        updatedSpot.setPricePerHour(pricePerHour);
//        deleteSpot(spotId);

//        ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();
//        List<Spot> spotList= parkingLot.getSpotList();
//        spotList.add(updatedSpot);
//        parkingLotRepository1.save(parkingLot);

        spotRepository1.save(updatedSpot);

        return updatedSpot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();

        List<Spot> spotList= parkingLot.getSpotList();

        for(Spot spot: spotList)
            spotRepository1.delete(spot);

        parkingLotRepository1.deleteById(parkingLotId);
    }
}
