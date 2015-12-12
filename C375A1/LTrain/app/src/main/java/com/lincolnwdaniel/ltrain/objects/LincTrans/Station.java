package com.lincolnwdaniel.ltrain.objects.LincTrans;

import com.lincolnwdaniel.ltrain.activities.StationActivity;
import com.lincolnwdaniel.ltrain.objects.TouchObject;
import com.lincolnwdaniel.ltrain.utils.handlers.LHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lwdthe1 on 9/8/2015.
 */
public class Station extends TouchObject {
    private State state;
    // a blocking queue to hold an unlimited amount of waiting passengers
    private LinkedBlockingQueue<Passenger> passengers;
    private int position;//position of the station on the train track
    private Name name;
    private int totalPassengers = 0; // total passengers waiting for the train at the station
    private int totalThroughPuts = 0; // the total amount of times passengers were admitted into the station

    public Station(Name name, int position){
        this.name = name;
        this.passengers = new LinkedBlockingQueue<>();

        this.state = State.ADMITTING;
        this.position = position;
    }

    public State getState() {
        return state;
    }

    /**
     * Sets the state of this station
     * @param state the State to put this station in
     * @param uiHandler The main UI handler that updates the UI with new data
     */
    public void setState(State state, LHandler uiHandler) {
        this.state = state;
        if(state.equals(State.ADMITTING)){
            if(name.equals(Name.SA)) {
                uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_ADMIT_STATION_A, "").sendToTarget();
            } else if(name.equals(Name.SB)){
                uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_ADMIT_STATION_B, "").sendToTarget();
            }
        }
    }

    /**
     *
     * @return the blocking queue of passengers waiting at this station
     */
    public LinkedBlockingQueue<Passenger> getPassengers() {
        return passengers;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Tries to admit a passenger into the station.
     * Sets the passenger state to waiting if successful.
     *
     * @param passenger The passenger to be admitted into the station
     * @param uiHandler The main UI handler that updates the UI with new data
     * @return true if successful. Because we are using a linked blocking queue, this will always succeed
     */
    public boolean admitPassenger(Passenger passenger, LHandler uiHandler){
        try {
            passengers.put(passenger);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            passenger.setState(Passenger.State.WAITING);
            updateOccupancy(uiHandler);
        }
        return false;
    }

    public void updateOccupancy(LHandler uiHandler) {
        if(name.equals(Name.SA)){
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_A_PASSENGERS_COUNT,
                    passengers.size()).sendToTarget();
        } else if (name.equals(Name.SB)) {
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_B_PASSENGERS_COUNT,
                    passengers.size()).sendToTarget();
        }
    }

    /**
     * The average amount of customers admitted into the station at a time
     * @param newPassengers Count of new customers admitted into the station this time around
     * @return
     */
    public int averageThroughput(int newPassengers){
        totalThroughPuts++;
        totalPassengers += newPassengers;
        return totalPassengers / totalThroughPuts;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Name getName() {
        return name;
    }

    public enum Name {
        SA, SB
    }

    /**
     * The many states a station can be in
     */
    public enum State {
        ADMITTING, BOARDING, UNBOARDING;
    }
}
