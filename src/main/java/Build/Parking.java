package Build;

import java.util.LinkedList;
import java.util.Queue;

public class Parking {
    private final String NAME;
    private final int CAPACITY;
    private final int DISABLED_PARKING_CAPACITY; // 5% of total capacity
    private int availableNormalParkingCapacity;
    private int availableDisabledParkingCapacity;

    // Car Thread Storage. FIFO Data Structure
    private final Queue<Car> CAR_STORAGE = new LinkedList<Car>();

    public Parking(final String NAME, final int CAPACITY) {
        this.NAME = NAME;
        this.CAPACITY = CAPACITY;
        this.DISABLED_PARKING_CAPACITY = (int) (CAPACITY * 0.05); // 5% of total capacity

        this.availableNormalParkingCapacity = this.CAPACITY - this.DISABLED_PARKING_CAPACITY;
        this.availableDisabledParkingCapacity = this.DISABLED_PARKING_CAPACITY;
    }
    // Thread-Safe way of assigning a parking spot to a normal client
    public synchronized void assignNormalSpot(final Car car) throws InterruptedException {
        while (this.availableNormalParkingCapacity <= 0) {
            wait(); // No spots available
        }
        availableNormalParkingCapacity--;
        CAR_STORAGE.add(car);
        System.out.println(COLOURS.ANSI_GREEN + car.getNAME() +  " added to the parking storage. There are now " +
                this.availableNormalParkingCapacity + " normal spots left" + COLOURS.ANSI_RESET);
        notify();
    }
    // Thread-Safe way of assigning a parking spot to a disabled client
    public synchronized void assignDisabledSpot(final Car car) throws InterruptedException {
        while (this.availableDisabledParkingCapacity <= 0 && this.availableNormalParkingCapacity <= 0) {
            wait(); // No parking spots for disabled / normal customers available
        }
        if (this.availableDisabledParkingCapacity > 0) {
            this.availableDisabledParkingCapacity--;
            System.out.println(COLOURS.ANSI_CYAN + "Disabled parking occupied by " + car.getNAME() +
                    ". There are " + this.availableDisabledParkingCapacity + " disabled spots left" + COLOURS.ANSI_RESET);
        } else if (this.availableNormalParkingCapacity > 0) {
            this.availableNormalParkingCapacity--;
            System.out.println(COLOURS.ANSI_CYAN + "No disabled spots left; " + car.getNAME() + " has used up a normal spot." +
                    " There are now " + this.availableNormalParkingCapacity + " spots left" + COLOURS.ANSI_RESET);
        }
        CAR_STORAGE.add(car);
        notify();
    }
    // Thread-Safe way of removing a car from the parking
    public synchronized void removeCarFromParking(final Car car) throws RuntimeException {
        if (CAR_STORAGE.remove(car)) { // remove() returns true if the item was successfully removed
            if (car.isDisabled()) {
                this.availableDisabledParkingCapacity++;
                System.out.println(COLOURS.ANSI_YELLOW + "Disabled spot freed. " + car.getNAME() +
                        " has left the parking" + COLOURS.ANSI_RESET);
            } else {
                this.availableNormalParkingCapacity++;
                System.out.println(COLOURS.ANSI_YELLOW + "Normal spot freed. " + car.getNAME() +
                        " has left the parking" + COLOURS.ANSI_RESET);
            }
            notify();
        }
    }
}
