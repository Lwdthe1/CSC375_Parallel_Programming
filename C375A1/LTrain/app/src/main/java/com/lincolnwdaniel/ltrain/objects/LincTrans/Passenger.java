package com.lincolnwdaniel.ltrain.objects.LincTrans;

import com.lincolnwdaniel.ltrain.objects.TouchObject;

/**
 * Created by lwdthe1 on 9/8/2015.
 */
public class Passenger extends TouchObject {
    private static int passengerIds = 0;
    private State state;
    private Station fromStation, toStation;
    private TicketType ticketType;

    public Passenger(Station fromStation, Station toStation, TicketType ticketType){
        this.id = passengerIds++;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.ticketType = ticketType;
        /*
            every passenger starts off outside of the station,
            and once inside, their state becomes WAITING
         */
        this.setState(State.OUTSIDE);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Only call when the passenger has arrived at their initial destination
     * and will be returning to the station they initially came from.
     */
    public boolean reverseDestination() {
        while(getState().equals(State.ARRIVED)) {
            if(getTicketType().equals(TicketType.ROUND_TRIP)) {
                Station tempStation = toStation;
                toStation = fromStation;
                fromStation = tempStation;
                setState(State.WAITING);
                setTicketType(TicketType.RETURN_ONE_WAY);
                return true;
            } else return false;
        }
        return false;
    }

    public boolean finishTrip() {
        while(getState().equals(State.ARRIVED) && !getTicketType().equals(TicketType.ROUND_TRIP)) {
            setState(Passenger.State.TRIP_FINISHED);
            return true;
        }
        return false;
    }


    public enum State{
        OUTSIDE, WAITING, SEATED, ARRIVED, TRIP_FINISHED;
    }

    public enum TicketType{
        ONE_WAY, ROUND_TRIP, RETURN_ONE_WAY;
    }
}
