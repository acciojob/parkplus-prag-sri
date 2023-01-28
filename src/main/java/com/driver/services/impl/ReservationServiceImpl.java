package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.

        if(userRepository3.findById(userId)==null)
            throw new Exception("Cannot make reservation");

        if(parkingLotRepository3.findById(parkingLotId)==null)
            throw new Exception("Cannot make reservation");

        User user= userRepository3.findById(userId).get();

        ParkingLot parkingLot= parkingLotRepository3.findById(parkingLotId).get();

        List<Spot> spotList= parkingLot.getSpotList();
        int assignedSpotId= 0;
        Spot assignedSpot= null;
        int totalPrice=Integer.MAX_VALUE;
        for(Spot spot: spotList)
        {
            int currWheels=Integer.MAX_VALUE;
            int pricePerHour= spot.getPricePerHour();
            if(spot.getSpotType()==SpotType.TWO_WHEELER)
                currWheels=2;
            else if(spot.getSpotType()==SpotType.FOUR_WHEELER)
                currWheels=4;
            if(spot.getOccupied()==false && currWheels>=numberOfWheels)
            {
                if(pricePerHour*timeInHours<totalPrice)
                {
                    assignedSpotId= spot.getId();
                    assignedSpot= spot;
                    totalPrice= pricePerHour*timeInHours;
                }
            }
        }

        if(assignedSpot==null)
            throw new Exception("Cannot make reservation");

        Reservation reservation= new Reservation(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(assignedSpot);
        reservationRepository3.save(reservation);

        assignedSpot.setOccupied(true);
        List<Reservation> reservationList= assignedSpot.getReservationList();
        reservationList.add(reservation);
        spotRepository3.save(assignedSpot);

        List<Reservation> reservations= user.getReservationList();
        reservations.add(reservation);
        userRepository3.save(user);

        return reservation;
    }
}
