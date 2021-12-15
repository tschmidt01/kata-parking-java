package victor.kata.parking;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to get a parking instance
 */
public class ParkingBuilder {

    private int size;
    private final List<Integer> pedestrianExits = new ArrayList<>();
    private final List<Integer> disabledBays = new ArrayList<>();

    public ParkingBuilder withSquareSize(final int size) {
        this.size = size;
        return this;
    }

    public ParkingBuilder withPedestrianExit(final int pedestrianExit) {
        this.pedestrianExits.add(pedestrianExit);
        return this;
    }

    public ParkingBuilder withDisabledBay(final int disabledBay) {
        this.disabledBays.add(disabledBay);
        return this;
    }

    public Parking build() {
        return new Parking(size, pedestrianExits, disabledBays);
    }
}
