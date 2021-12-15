package victor.kata.parking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParkingBuilderTest {

    @Test
    public void testBuildBasicParking() {
        final Parking parking = new ParkingBuilder().withSquareSize(4).build();
        assertEquals(16, parking.getAvailableBays());
    }

    @Test
    public void testBuildParkingWithPedestrianExit1() {
        final Parking parking = new ParkingBuilder()
                .withSquareSize(3)
                .withPedestrianExit(5)
                .build();
        assertEquals(8, parking.getAvailableBays());
    }

    @Test
    public void testBuildParkingWithPedestrianExit2() {
        final Parking parking = new ParkingBuilder()
                .withSquareSize(3)
                .withPedestrianExit(5)
                .withPedestrianExit(10)
                .build();
        assertEquals(7, parking.getAvailableBays());
    }

    @Test
    public void testBuildParkingWithDisabledSlot() {
        final Parking parking = new ParkingBuilder()
                .withSquareSize(2)
                .withDisabledBay(2)
                .build();
        assertEquals(4, parking.getAvailableBays());
    }

    @Test
    public void testBuildParkingWithPedestrianExitsAndDisabledSlots() {
        final Parking parking = new ParkingBuilder()
                .withSquareSize(10)
                .withPedestrianExit(8)
                .withPedestrianExit(42)
                .withPedestrianExit(85)
                .withDisabledBay(2)
                .withDisabledBay(47)
                .withDisabledBay(72)
                .build();
        assertEquals(97, parking.getAvailableBays());
    }

}
