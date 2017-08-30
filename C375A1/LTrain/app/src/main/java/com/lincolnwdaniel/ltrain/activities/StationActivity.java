package com.lincolnwdaniel.ltrain.activities;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincolnwdaniel.ltrain.LTrainApplication;
import com.lincolnwdaniel.ltrain.R;
import com.lincolnwdaniel.ltrain.objects.LincTrans.Passenger;
import com.lincolnwdaniel.ltrain.objects.LincTrans.Station;
import com.lincolnwdaniel.ltrain.objects.LincTrans.Train;
import com.lincolnwdaniel.ltrain.utils.handlers.LHandler;


public class StationActivity extends AppCompatActivity {
    private static final int MAX_TRACK_POSITIONS = 1000;
    public static final int HANDLER_MESSAGE_UPDATE_TRAIN_STATE = 100;
    public static final int HANDLER_MESSAGE_UPDATE_STATION_A_PASSENGERS_COUNT = 210, HANDLER_MESSAGE_UPDATE_STATION_B_PASSENGERS_COUNT = 220;
    public static final int HANDLER_MESSAGE_UPDATE_TRAIN_PASSENGERS_COUNT = 330,
            HANDLER_MESSAGE_UPDATE_TRAIN_SERVICED_PASSENGERS_COUNT = 340;
    public static final int HANDLER_MESSAGE_ADMIT_STATION_A = 410, HANDLER_MESSAGE_ADMIT_STATION_B = 420;
    public static final int HANDLER_MESSAGE_UPDATE_STATION_A_AVG_THROUGHPUT_TEXT = 510,
            HANDLER_MESSAGE_UPDATE_STATION_B_AVG_THROUGHPUT_TEXT = 520,
            HANDLER_MESSAGE_UPDATE_STATION_A_RETURNERS = 610,
            HANDLER_MESSAGE_UPDATE_STATION_B_RETURNERS = 620,
            HANDLER_MESSAGE_UPDATE_STATION_A_LEAVING_PASSENGERS_TEXT = 710,
            HANDLER_MESSAGE_UPDATE_STATION_B_LEAVING_PASSENGERS_TEXT = 720;


    private Station mStationA;
    private Station mStationB;
    private Train mTrain;

    private int STATION_THROUGHPUT_SLEEP = 3_000;
    private final int NUM_TRAIN_SEATS = 450;
    private int TRAIN_MIN_SPEED = 25, TRAIN_MAX_SPEED = 20; //lower = faster

    //the master ui handler handles most UI updates, including the train
    private LHandler mMasterUiHandler,
    //the station A handler handles only station A UI updates
    mStationAHandler,
    //the station B handler handles only station B UI updates
    mStationBHandler;

    private Thread mMasterThread, mTrain1Thread, mStationAThread, mStationBThread;

    private static String TAG = "Station Activity";
    private ImageView mTrainPos_0, mTrainPos_40, mTrainPos_90, mTrainPos_200,mTrainPos_210, mTrainPos_250, mTrainPos_390,
            mTrainPos_500, mTrainPos_600, mTrainPos_680, mTrainPos_715 , mTrainPos_750, mTrainPos_890;
    private Context mContext;
    private TextView mTrainStateTextView;
    private TextView mTrainPassengersCountTextView, mStationAPassengersCountTextView,
            mStationBPassengersCountTextView, mStationALeavingPassengersTextView, mStationBLeavingPassengersTextView;
    private LinearLayout mContainerConductor;


    private TextView mStationAAvgThroughputTextView, mStationBAvgThroughputTextView;
    private int mTrainCurrentSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        mContext = this;
        initHandlers();
        initLTrainSystem();
        initViews();
        startMasterThread();
    }

    /**
     * Initializes our train stations and our train
     */
    private void initLTrainSystem() {
        mStationA = new Station(Station.Name.SA, 200);
        mStationB = new Station(Station.Name.SB, 680);
        mTrain = new Train(mContext, 1, NUM_TRAIN_SEATS, mStationB, mStationA, mMasterUiHandler);
    }

    private void initViews() {
        mTrainStateTextView = (TextView) findViewById(R.id.train_state);
        mTrainPassengersCountTextView = (TextView) findViewById(R.id.train_passengers_count);
        mStationAPassengersCountTextView = (TextView) findViewById(R.id.station_a_passengers_count);
        mStationBPassengersCountTextView = (TextView) findViewById(R.id.station_b_passengers_count);
        mStationAAvgThroughputTextView = (TextView) findViewById(R.id.station_a_avg_throughput);
        mStationBAvgThroughputTextView = (TextView) findViewById(R.id.station_b_avg_throughput);
        mStationALeavingPassengersTextView = (TextView) findViewById(R.id.station_a_leaving_passengers);
        mStationBLeavingPassengersTextView = (TextView) findViewById(R.id.station_b_leaving_passengers);
        mContainerConductor = (LinearLayout) findViewById(R.id.container_conductor);
    }

    /**
     * Creates the handlers that will update the system's user interface (UI)
     */
    private void initHandlers() {
        mMasterUiHandler = new LHandler() {
            @Override
            public void handleTheMessage(Message msg) {
                switch (msg.what){
                    case HANDLER_MESSAGE_UPDATE_TRAIN_STATE:
                        mTrainStateTextView.setText(msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_TRAIN_PASSENGERS_COUNT:
                        mTrainPassengersCountTextView.setText(msg.obj.toString() + " Aboard");
                        break;
                    case HANDLER_MESSAGE_UPDATE_TRAIN_SERVICED_PASSENGERS_COUNT:
                        ((TextView) findViewById(R.id.train_serviced_passengers_count)).setText(msg.obj.toString() + " Serviced");
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_A_PASSENGERS_COUNT:
                        mStationAPassengersCountTextView.setText("TP: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_B_PASSENGERS_COUNT:
                        mStationBPassengersCountTextView.setText("TP: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_A_RETURNERS:
                        ((TextView) findViewById(R.id.station_a_returners)).setText("NR: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_B_RETURNERS:
                        ((TextView) findViewById(R.id.station_b_returners)).setText("NR: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_A_AVG_THROUGHPUT_TEXT:
                        mStationAAvgThroughputTextView.setText("AT: "
                                + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_B_AVG_THROUGHPUT_TEXT:
                        mStationBAvgThroughputTextView.setText("AT: "
                                + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_A_LEAVING_PASSENGERS_TEXT:
                        mStationALeavingPassengersTextView.setText("PL: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_UPDATE_STATION_B_LEAVING_PASSENGERS_TEXT:
                        mStationBLeavingPassengersTextView.setText("PL: " + msg.obj.toString());
                        break;
                    case HANDLER_MESSAGE_ADMIT_STATION_A:
                        admitPassengersToStation(mStationA);
                        break;
                    case HANDLER_MESSAGE_ADMIT_STATION_B:
                        admitPassengersToStation(mStationB);
                        break;
                }
            }
        };

        mStationAHandler = new LHandler() {
            @Override
            public void handleTheMessage(Message msg) {
                switch (msg.what){

                }
            }
        };

        mStationBHandler = new LHandler() {
            @Override
            public void handleTheMessage(Message msg) {
                switch (msg.what){

                }
            }
        };
    }

    /**
     * Starts the master thread
     */
    private void startMasterThread() {
        mMasterThread = new Thread() {
            @Override
            public void run() {
                super.run();
                createStationAThread();
                createStationBThread();
                createTrain1Thread();

                mStationAThread.start();
                mStationBThread.start();
                initTrainPositions();
                try {
                    Log.v(TAG + "Master", "Master Thread sleeping for 10000ms before starting Train Thread");
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mTrain1Thread.start();
                }

            }
        };
        mMasterThread.start();
    }


    /**
     * Creates a thread to handle activity in Station A
     */
    private void createStationAThread() {
        mStationAThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.v(TAG + "StationA", "Station A state = " + mStationA.getState());
                attemptAdmitPassengers();
            }

            private void attemptAdmitPassengers() {
                for (;;) {
                    admitPassengersToStation(mStationA);
                    try {
                        sleep(STATION_THROUGHPUT_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        };
    }

    /**
     * Creates a thread to handle activity in Station B
     */
    private void createStationBThread() {
        mStationBThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.v(TAG + "StationB", "Station B state = " + mStationB.getState());
                attemptAdmitPassengers();
            }

            private void attemptAdmitPassengers() {
                for (;;) {
                    admitPassengersToStation(mStationB);
                    try {
                        sleep(STATION_THROUGHPUT_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }
        };
    }

    /**
     * Tries to admit new passengers to the provided station while the station is in the State of Admitting
     * @param station
     */
    private void admitPassengersToStation(final Station station) {
        Thread admitPassengersToStation = new Thread() {
            LHandler stationHandler = null;
            Station fromStation = null, toStation = null;
            int maxThroughput = 10;
            @Override
            public void run() {
                if(station.getState().equals(Station.State.ADMITTING)){
                    switch(station.getName()) {
                        case SA:
                            stationHandler = mStationAHandler;
                            fromStation = mStationA;
                            toStation = mStationB;
                            maxThroughput = 50;
                            break;
                        case SB:
                            stationHandler = mStationBHandler;
                            fromStation = mStationB;
                            toStation = mStationA;
                            maxThroughput = 40;
                            break;
                    }
                    stationHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int numNewPassengers = LTrainApplication.getRandGen().nextInt(maxThroughput)+1;
                            int ticketTypeInt = -1;
                            final int finalNumNewPassengers = numNewPassengers;

                            while(numNewPassengers > 0) {
                                ticketTypeInt = LTrainApplication.getRandGen().nextInt(2)+1;
                                Passenger.TicketType ticketType = ticketTypeInt < 2 ? Passenger.TicketType.ONE_WAY : Passenger.TicketType.ROUND_TRIP;
                                station.admitPassenger(new Passenger(fromStation, toStation, ticketType), mMasterUiHandler);
                                numNewPassengers--;
                            }

                            if(station.getName().equals(Station.Name.SA)) {
                                mMasterUiHandler.obtainMessage(
                                        HANDLER_MESSAGE_UPDATE_STATION_A_AVG_THROUGHPUT_TEXT,
                                        station.averageThroughput(finalNumNewPassengers))
                                        .sendToTarget();
                            } else if(station.getName().equals(Station.Name.SB)) {
                                mMasterUiHandler.obtainMessage(
                                        HANDLER_MESSAGE_UPDATE_STATION_B_AVG_THROUGHPUT_TEXT,
                                        station.averageThroughput(finalNumNewPassengers))
                                        .sendToTarget();
                            }
                        }
                    });
                }
            }
        };
        admitPassengersToStation.start();
    }

    /**
     * Creates a thread to handle train activity
     */
    private void createTrain1Thread() {
        mTrain1Thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (mTrain.getState().equals(Train.State.GOING)) {
                    mTrain.setPosition(moveTrainPositionOnTrack(mTrain.getPosition()));
                    //Log.v(TAG + "Train", "Train position = " + mTrain.getPosition());

                    mMasterUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showCurrentTrainPosition(mTrain.getPosition());
                        }
                    });

                    if (mTrain.getPosition() == mStationA.getPosition()) {
                        //Log.v(TAG + "Train", "Arrived at Station A " + mTrain.getToStation().getName().toString());
                        /*mMasterUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                LToast.showL(getApplicationContext(), "Arrived at Station A " + mTrain.getToStation().getName().toString());
                            }
                        });*/

                        unboardAndBoardTrainAtStation();
                        mTrain.setState(Train.State.GOING, mMasterUiHandler);
                    } else if (mTrain.getPosition() == mStationB.getPosition()) {
                        unboardAndBoardTrainAtStation();
                        mTrain.setState(Train.State.GOING, mMasterUiHandler);
                    }
                    try {
                        //put train thread to sleep for any amount of time between their min and max speed
                        //this simulates the speed of the train
                        int speed = LTrainApplication.getRandGen().nextInt(TRAIN_MIN_SPEED) + (TRAIN_MIN_SPEED/2);
                        mTrainCurrentSpeed = speed < 0 ? TRAIN_MIN_SPEED : speed;
                        sleep(mTrainCurrentSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * Unboards all passengers on the train and initiates boarding at the current station
     */
    private void unboardAndBoardTrainAtStation() {
        mMasterUiHandler.post(new Runnable() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        CountDownTimer countDown = new CountDownTimer(10_000, 1000) {

                            /**
                             * Callback fired on regular interval.
                             *
                             * @param millisUntilFinished The amount of time until finished.
                             */
                            @Override
                            public void onTick(long millisUntilFinished) {
                                //Log.v(TAG + "Train", "Conductor leaving in " + millisUntilFinished + "ms");
                            }

                            /**
                             * Callback fired when the time is up.
                             */
                            @Override
                            public void onFinish() {
                                if (mTrain.getState() != Train.State.GOING) {
                                    //Log.v(TAG + "Train", "Conductor pushed start");
                                    mTrain.setState(Train.State.GOING, mMasterUiHandler);
                                    mMasterUiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mContainerConductor.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        };
                        countDown.start();
                    }
                }.run();
            }
        });

        mTrain.setState(Train.State.UNBOARDING, mMasterUiHandler);
        mTrain.unboardPassengers(mTrain1Thread, mMasterUiHandler);
        mTrain.boardPassengers(mTrain1Thread, mMasterUiHandler);
    }

    /**
     * Increments the train position by 1
     * @param position
     * @return returns the train position plus one
     */
    private int moveTrainPositionOnTrack(int position) {
        if (position < MAX_TRACK_POSITIONS) {
            int newPos = position + 1;
            return newPos;
        }
        return 0;
    }

    /**
     * Initializes the views for the train positions for the UI
     */
    private void initTrainPositions() {
        mTrainPos_0 = (ImageView) findViewById(R.id.train_pos_0);
        mTrainPos_40 = (ImageView) findViewById(R.id.train_pos_40);
        mTrainPos_90 = (ImageView) findViewById(R.id.train_pos_90);
        mTrainPos_200 = (ImageView) findViewById(R.id.train_pos_200);
        mTrainPos_210 = (ImageView) findViewById(R.id.train_pos_210);
        mTrainPos_250 = (ImageView) findViewById(R.id.train_pos_250);
        mTrainPos_390 = (ImageView) findViewById(R.id.train_pos_390);
        mTrainPos_500 = (ImageView) findViewById(R.id.train_pos_500);
        mTrainPos_600 = (ImageView) findViewById(R.id.train_pos_600);
        mTrainPos_680 = (ImageView) findViewById(R.id.train_pos_680);
        mTrainPos_715 = (ImageView) findViewById(R.id.train_pos_715);
        mTrainPos_750 = (ImageView) findViewById(R.id.train_pos_750);
        mTrainPos_890 = (ImageView) findViewById(R.id.train_pos_890);
    }

    /**
     * Shows the train's current position in the UI
     * @param trainPos
     */
    private void showCurrentTrainPosition(int trainPos) {
        mTrainPos_0.setVisibility(View.INVISIBLE);
        mTrainPos_40.setVisibility(View.INVISIBLE);
        mTrainPos_90.setVisibility(View.INVISIBLE);
        mTrainPos_200.setVisibility(View.INVISIBLE);
        mTrainPos_210.setVisibility(View.INVISIBLE);
        mTrainPos_250.setVisibility(View.INVISIBLE);
        mTrainPos_390.setVisibility(View.INVISIBLE);
        mTrainPos_500.setVisibility(View.INVISIBLE);
        mTrainPos_600.setVisibility(View.INVISIBLE);
        mTrainPos_680.setVisibility(View.INVISIBLE);
        mTrainPos_715.setVisibility(View.INVISIBLE);
        mTrainPos_750.setVisibility(View.INVISIBLE);
        mTrainPos_890.setVisibility(View.INVISIBLE);

        if (trainPos >= 890) {
            mTrainPos_890.setVisibility(View.VISIBLE);
            mContainerConductor.setVisibility(View.INVISIBLE);
        } else if (trainPos >= 750) {
            mTrainPos_750.setVisibility(View.VISIBLE);
        } else if (trainPos >= 715) {
            mTrainPos_715.setVisibility(View.VISIBLE);
        } else if (trainPos >= 680) {
            mTrainPos_680.setVisibility(View.VISIBLE);
            mTrainCurrentSpeed = 0;
        } else if (trainPos >= 600) {
            mTrainPos_600.setVisibility(View.VISIBLE);
        } else if (trainPos >= 500) {
            mTrainPos_500.setVisibility(View.VISIBLE);
        } else if (trainPos >= 390) {
            mTrainPos_390.setVisibility(View.VISIBLE);
            mContainerConductor.setVisibility(View.INVISIBLE);
        } else if (trainPos >= 250) {
            mTrainPos_250.setVisibility(View.VISIBLE);
        } else if (trainPos >= 210) {
            mTrainPos_210.setVisibility(View.VISIBLE);
        } else if (trainPos >= 200) {
            mTrainPos_200.setVisibility(View.VISIBLE);
            mTrainCurrentSpeed = 0;
        } else if (trainPos >= 90) {
            mTrainPos_90.setVisibility(View.VISIBLE);
        } else if (trainPos >= 40) {
            mTrainPos_40.setVisibility(View.VISIBLE);
        } else {
            mTrainPos_0.setVisibility(View.VISIBLE);
        }
        if(trainPos % 10 == 0 || Integer.parseInt(((TextView) findViewById(R.id.train_speed)).getText().toString().replaceAll("\\D+","")) == 0 ){
            updateUITrainSpeed();
        }
    }

    /**
     * Updates the train's current speed in the UI
     */
    private void updateUITrainSpeed() {
        mMasterUiHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.train_speed)).setText(mTrainCurrentSpeed + " mph");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
