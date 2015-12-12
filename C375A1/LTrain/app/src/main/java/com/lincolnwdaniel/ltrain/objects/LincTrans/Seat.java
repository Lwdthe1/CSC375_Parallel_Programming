package com.lincolnwdaniel.ltrain.objects.LincTrans;

import com.lincolnwdaniel.ltrain.objects.TouchObject;

/**
 * Created by lwdthe1 on 9/8/2015.
 */
public class Seat extends TouchObject {

    private State state;
    private int trainId;
    private Passenger passenger;

    public Seat(int trainId) {
        this.trainId = trainId;
        this.state = State.EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Seat seatPassenger(Passenger passenger) {
        this.passenger = passenger;
        return this;
    }

    public void updateState() {
        if(this.passenger != null) setState(State.TAKEN);
    }

    public Passenger getPassenger() {
        return passenger;
    }

    /**
     * Removes the passenger from the seat and returns it.
     * @return
     */
    public Passenger unseatPassenger() {
        Passenger temp = passenger;
        this.passenger = null;
        this.setState(State.EMPTY);
        return temp;

    }

    public enum State {
        EMPTY, TAKEN;
    }
}
