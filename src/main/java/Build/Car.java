package Build;

public class Car extends Thread {
    private final String NAME;
    private final boolean IS_DISABLED;
    private final int PARKING_TIME;
    private final Parking PARKING;

    public Car(final String NAME, final Parking PARKING) {
        this.NAME = NAME;
        this.IS_DISABLED = setDISABLED();
        this.PARKING_TIME = setInsideParkingTime();
        this.PARKING = PARKING;
    }

    private boolean setDISABLED() {
        if (Math.random() <= 0.10) { // 10%
            System.out.println(COLOURS.ANSI_RED + NAME + " is disabled " + COLOURS.ANSI_RESET);
            return true;
        }
        return false;
    }
    // Waiting time between 1 to 5s
    private int setInsideParkingTime() {
        int minTime = 1000;
        int maxTime = 5000;
        return minTime + (int)(Math.random() * (maxTime - minTime));
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (IS_DISABLED) {
                    PARKING.assignDisabledSpot(this);
                } else {
                    PARKING.assignNormalSpot(this);
                }
                Thread.sleep(this.PARKING_TIME);
                PARKING.removeCarFromParking(this);
                Thread.sleep(outsideWaitingTime());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int outsideWaitingTime() {
        int minTime = 2000;
        int maxTime = 7000;
        return minTime + (int)(Math.random() * (maxTime - minTime));
    }

    public boolean isDisabled() {
        return IS_DISABLED;
    }

    public String getNAME() {
        return NAME;
    }
}