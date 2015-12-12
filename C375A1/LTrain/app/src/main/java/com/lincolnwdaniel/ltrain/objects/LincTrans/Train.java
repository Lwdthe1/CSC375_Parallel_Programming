package com.lincolnwdaniel.ltrain.objects.LincTrans;

import android.content.Context;
import android.util.Log;

import com.lincolnwdaniel.ltrain.activities.StationActivity;
import com.lincolnwdaniel.ltrain.objects.TouchObject;
import com.lincolnwdaniel.ltrain.utils.handlers.LHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by lwdthe1 on 9/8/2015.
 */
public class Train extends TouchObject {
    private static final String TAG = "Train";
    private State state;
    private Station fromStation, toStation;
    private ArrayBlockingQueue<Seat> seats;// a blocking queue to hold a limit amount of seats holding passengers
    private int position;//position of the train on the train track
    private int servicedPassengers;
    private Context mContext; //the context of the train

    public Train(Context context, int id, int numSeats, Station fromStation, Station toStation, LHandler uiHandler){
        this.mContext = context;

        this.id = id;
        initSeats(numSeats);
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.position = 0;
        this.servicedPassengers = 0;
        setState(State.GOING, uiHandler);
    }

    private void initSeats(int numSeats) {
        seats = new ArrayBlockingQueue<>(numSeats);
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

    public void setState(State state, LHandler uiHandler) {
        this.state = state;
        uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_TRAIN_STATE, state).sendToTarget();
    }

    public Station getFromStation() {
        return fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     *
     * @param thread The thread this method is being called from. Used to sleep at will
     * @param uiHandler The main UI handler that updates the UI with new data
     * @return the amount of passengers unboarded from the train (should always be all the passengers)
     */
    public int unboardPassengers(Thread thread, LHandler uiHandler){
        int numUnboarded = 0;
        int numNewReturners = 0;
        while(getState().equals(State.UNBOARDING)){
            //Log.v(TAG + "#unboard", "Unboarding passengers to Station " + toStation.getName().toString());

            //the station we are at now is the toStation
            while(seats.size() > 0) {
                Seat seat = seats.remove();

                Passenger passenger = seat.unseatPassenger();
                if(passenger != null) {
                    passenger.setState(Passenger.State.ARRIVED);
                    if (passenger.getTicketType().equals(Passenger.TicketType.ROUND_TRIP)) {
                        passenger.reverseDestination();
                        toStation.admitPassenger(passenger, uiHandler);
                        numNewReturners++;
                        //Log.v(TAG+"#unBoard", "Passenger " + passenger.getId() + " = round trip");
                    } else {
                        passenger.finishTrip();
                    }
                    numUnboarded++;
                }

            }
            servicedPassengers += numUnboarded;
            updateUIAfterUnboarding(uiHandler, numUnboarded, numNewReturners);
            finishUnboarding(thread, uiHandler);
            break;
        }
        return numUnboarded;
    }

    /**
     * Puts the thread to sleep to allow user to see updates.
     * Reverses the destination of the train.
     * Changes the train's state to Boarding
     * @param thread The thread this method is being called from. Used to sleep at will
     * @param uiHandler The main UI handler that updates the UI with new data
     */
    private void finishUnboarding(Thread thread, LHandler uiHandler) {
        try {
            //strictly for UX purposes.
            //This allows the user to see that the unboarding process did indeed run
            thread.sleep(1_500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //makes the current station equal the fromStation
            //and the current fromStation = the toStation
            reverseDestination();
            //set state of the train to Boarding
            //so passengers waiting at the current station can board
            setState(State.BOARDING, uiHandler);
        }
    }

    /**
     * Update sthe UI after unboarding all the passengers from the train
     * @param uiHandler The main UI handler that updates the UI with new data
     * @param numNewReturners
     */
    private void updateUIAfterUnboarding(LHandler uiHandler, int numUnboarded, int numNewReturners) {
        uiHandler.obtainMessage(
                StationActivity.HANDLER_MESSAGE_UPDATE_TRAIN_PASSENGERS_COUNT
                , 0).sendToTarget();
        uiHandler.obtainMessage(
                StationActivity.HANDLER_MESSAGE_UPDATE_TRAIN_SERVICED_PASSENGERS_COUNT
                , servicedPassengers).sendToTarget();

        if(toStation.getName().equals(Station.Name.SA)) {
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_A_RETURNERS,
                    numNewReturners).sendToTarget();
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_A_LEAVING_PASSENGERS_TEXT,
                    numUnboarded - numNewReturners).sendToTarget();
            if(numNewReturners > 0) {
                int avg = toStation.averageThroughput(numNewReturners);
                //Log.v(TAG, "Station " + toStation.getName() + " AT = " + avg);
                uiHandler.obtainMessage(
                        StationActivity.HANDLER_MESSAGE_UPDATE_STATION_A_AVG_THROUGHPUT_TEXT,
                        avg)
                        .sendToTarget();
            }
        } else {
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_B_RETURNERS,
                    numNewReturners).sendToTarget();
            uiHandler.obtainMessage(StationActivity.HANDLER_MESSAGE_UPDATE_STATION_B_LEAVING_PASSENGERS_TEXT,
                    numUnboarded - numNewReturners).sendToTarget();
            if(numNewReturners > 0) {
                int avg = toStation.averageThroughput(numNewReturners);
                //Log.v(TAG, "Station " + toStation.getName() + " AT = " + avg);
                uiHandler.obtainMessage(
                        StationActivity.HANDLER_MESSAGE_UPDATE_STATION_B_AVG_THROUGHPUT_TEXT,
                        avg)
                        .sendToTarget();
            }
        }
    }

    /**
     * The fromStation is the current station the train is at.
     * After the train has unboarded all passengers,
     * this method boards all the passengers waiting at the current station
     * until this train is full or conductor pushes start.
     * @return
     * @param thread The thread this method is being called from. Used to sleep at will
     * @param uiHandler The main UI handler that updates the UI with new data
     */
    public int boardPassengers(Thread thread, final LHandler uiHandler){
        int numBoarded = 0;

        while(state.equals(State.BOARDING)) {
            //Log.v(TAG + "Train", "Boarding passengers at Station " + fromStation.getName().toString() + " | numBoarded = " + numBoarded);
            while (fromStation.getPassengers().size() > 0){
                if (getState() != State.GOING) {
                    Passenger passenger = null;
                    try {
                        //Log.v(TAG + "Train" + numBoarded, "Taking passenger from " + fromStation.getName());
                        passenger = fromStation.getPassengers().take();
                        //Log.v(TAG + "Train" + numBoarded, "Took passenger " + passenger.getId() + " from " + fromStation.getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.v(TAG + "Train" + numBoarded, "Boarding intterrupted at " + fromStation.getName());
                    } finally {
                        fromStation.updateOccupancy(uiHandler);
                        boolean seatedPassenger = seatPassenger(passenger, uiHandler);

                        if(seatedPassenger) {//success
                            passenger.setState(Passenger.State.SEATED);
                            numBoarded++;
                            uiHandler.obtainMessage(
                                    StationActivity.HANDLER_MESSAGE_UPDATE_TRAIN_PASSENGERS_COUNT
                                    , numBoarded).sendToTarget();
                            if (numBoarded > seats.size()) {
                                setState(State.GOING, uiHandler);
                            }
                        } else {
                            setState(State.GOING, uiHandler);
                        }
                    }
                } else break;
            }
            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fromStation.updateOccupancy(uiHandler);
        //conductorThread.interrupt();
        return numBoarded;
    }

    /**
     *
     * @param passenger
     * @param uiHandler The main UI handler that updates the UI with new data
     * @return
     */
    public boolean seatPassenger(Passenger passenger, LHandler uiHandler) {
        try {
            seats.add(new Seat(id).seatPassenger(passenger));
        } catch (IllegalStateException e){
            return false;
        } finally {
            return  true;
        }
    }

    /**
     * Reverses the direction of the train
     * @return true if the direction was reversed successfully
     */
    public boolean reverseDestination() {
        while(getState().equals(Train.State.UNBOARDING)) {
            Station tempStation = toStation;
            toStation = fromStation;
            fromStation = tempStation;
            tempStation = null;
            return true;
        }
        return false;
    }


    /**
     * The many states a train can be in
     */
    public enum State {
        GOING, UNBOARDING, BOARDING;
    }
}
