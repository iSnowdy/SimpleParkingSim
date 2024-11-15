package Run;

import Build.Car;
import Build.Parking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        args = new String[]{"5", "20"};
        if (!validateArgs(args)) {
            System.err.println("Invalid CLI");
            System.exit(1);
        }
        int parkingCapacity = Integer.parseInt(args[0]);
        int carThreads = Integer.parseInt(args[1]);

        Parking parking = new Parking("Tomorrowland", parkingCapacity);

        for (int i = 0; i < carThreads; i++) {
            Car car = new Car("Car " + (i + 1), parking);
            car.start();
        }
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) return false;
        return args[0].matches("^\\d+$") && args[1].matches("^\\d+$");
    }
}