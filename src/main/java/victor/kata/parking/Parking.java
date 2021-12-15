package victor.kata.parking;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Handles the parking mechanisms: park/unpark a car (also for disabled-only bays) and provides a string representation of its state.
 */
public class Parking {

    private final int size;
    private final List<Integer> pedestrianExits;
    private final List<Integer> disabledBays;
    private final Map<Integer, Character> parkedBays = new HashMap<>();

    public Parking(int size, List<Integer> pedestrianExits, List<Integer> disabledBays) {
        this.size = size;
        this.pedestrianExits = pedestrianExits;
        this.disabledBays = disabledBays;
    }

    /**
     * @return the number of available parking bays left
     */
    public int getAvailableBays() {
        return (this.size * this.size) - pedestrianExits.size() - this.parkedBays.size();
    }

    /**
     * Park the car of the given type ('D' being dedicated to disabled people) in closest -to pedestrian exit- and first (starting from the parking's entrance)
     * available bay. Disabled people can only park on dedicated bays.
     *
     * @param carType the car char representation that has to be parked
     * @return bay index of the parked car, -1 if no applicable bay found
     */
    public int parkCar(final char carType) {
        Optional<Integer> bayToParkIn = carType != 'D' ? getNextFreeParkingBay() : getNextFreeDisabledParkingBay().or(this::getNextFreeParkingBay);
        bayToParkIn.ifPresent(it -> parkedBays.put(it, carType));
        return bayToParkIn.orElse(-1);
    }

    private Optional<Integer> getNextFreeDisabledParkingBay() {
        return this.disabledBays.stream()
                .filter(it -> !parkedBays.containsKey(it))
                .min(Comparator.comparingInt(this::getMinDistanceToNextPedExit));
    }

    private Optional<Integer> getNextFreeParkingBay() {
        return IntStream.range(0, this.size * size)
                .boxed()
                .filter(it -> !parkedBays.containsKey(it))
                .filter(it -> !pedestrianExits.contains(it))
                .min(Comparator.comparingInt(this::getMinDistanceToNextPedExit));
    }

    private int getMinDistanceToNextPedExit(Integer it) {
        return this.pedestrianExits.stream()
                .mapToInt(ped -> ped)
                .map(ped -> Math.abs(ped - it))
                .min()
                .orElse(it);
    }

    /**
     * Unpark the car from the given index
     *
     * @param index
     * @return true if a car was parked in the bay, false otherwise
     */
    public boolean unparkCar(final int index) {
        Optional<Integer> carInBay = this.parkedBays.keySet().stream()
                .filter(it -> it.equals(index))
                .findFirst();

        carInBay.ifPresent(this.parkedBays::remove);
        return carInBay.isPresent();
    }

    /**
     * Print a 2-dimensional representation of the parking with the following rules:
     * <ul>
     * <li>'=' is a pedestrian exit
     * <li>'@' is a disabled-only empty bay
     * <li>'U' is a non-disabled empty bay
     * <li>'D' is a disabled-only occupied bay
     * <li>the char representation of a parked vehicle for non-empty bays.
     * </ul>
     * U, D, @ and = can be considered as reserved chars.
     * <p>
     * Once an end of lane is reached, then the next lane is reversed (to represent the fact that cars need to turn around)
     *
     * @return the string representation of the parking as a 2-dimensional square. Note that cars do a U turn to continue to the next lane.
     */
    @Override
    public String toString() {
        return IntStream.range(0, this.size * size).boxed()
                .collect(Collectors.groupingBy(it -> it / size))
                .entrySet().stream()
                .map(e -> e.getKey() % 2 == 0 ? e.getValue() : reverseList(e))
                .map(this::mapLineToString)
                .collect(Collectors.joining("\n"));
    }

    private List<Integer> reverseList(Map.Entry<Integer, List<Integer>> e) {
        return e.getValue().stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    private String mapLineToString(List<Integer> it) {
        return it.stream()
                .map(this::mapIndexToChar)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private Character mapIndexToChar(Integer it) {
        if (pedestrianExits.contains(it)) {
            return '=';
        } else if (disabledBays.contains(it) && !parkedBays.containsKey(it)) {
            return '@';
        } else return parkedBays.getOrDefault(it, 'U');
    }
}
