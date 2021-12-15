package victor.kata.parking

import kotlin.math.abs

/**
 * Handles the parking mechanisms: park/unpark a car (also for disabled-only bays) and provides a string representation of its state.
 */
class Parking(private val size: Int, private val pedExits: Set<Int>, private val disabledBays: Set<Int>) {

    private val parkedBays: MutableMap<Int, Char> = mutableMapOf()

    /**
     * @return the number of available parking bays left
     */
    fun getAvailableBays(): Int = size * size - pedExits.size - parkedBays.size

    /**
     * Park the car of the given type ('D' being dedicated to disabled people) in closest -to pedestrian exit- and first (starting from the parking's entrance)
     * available bay. Disabled people can only park on dedicated bays.
     *
     *
     * @param carType
     * the car char representation that has to be parked
     * @return bay index of the parked car, -1 if no applicable bay found
     */
    fun parkCar(carType: Char): Int {
        return getNextFreeBayForCar(carType)
            ?.let {
                parkedBays[it] = carType
                it
            } ?: -1
    }

    private fun getNextFreeBayForCar(carType: Char): Int? {
        return if (carType == 'D') {
            getNextFreeDisabledBay() ?: getNextFreeNormalBay()
        } else {
            getNextFreeNormalBay()
        }
    }

    private fun getNextFreeDisabledBay(): Int? {
        return disabledBays
            .filter { !parkedBays.contains(it) }
            .minByOrNull { getMinDistanceToNextPedExit(it) }
    }

    private fun getNextFreeNormalBay(): Int? {
        return allIndexes()
            .filter { !parkedBays.contains(it) }
            .filter { !pedExits.contains(it) }
            .minByOrNull { getMinDistanceToNextPedExit(it) }
    }

    private fun getMinDistanceToNextPedExit(it: Int): Int {
        return this.pedExits.minOfOrNull { ped -> abs(ped - it) } ?: it
    }

    /**
     * Unpark the car from the given index
     *
     * @param index
     * @return true if a car was parked in the bay, false otherwise
     */
    fun unparkCar(index: Int): Boolean {
        return parkedBays.remove(index) != null
    }

    /**
     * Print a 2-dimensional representation of the parking with the following rules:
     *
     *  * '=' is a pedestrian exit
     *  * '@' is a disabled-only empty bay
     *  * 'U' is a non-disabled empty bay
     *  * 'D' is a disabled-only occupied bay
     *  * the char representation of a parked vehicle for non-empty bays.
     *
     * U, D, @ and = can be considered as reserved chars.
     *
     * Once an end of lane is reached, then the next lane is reversed (to represent the fact that cars need to turn around)
     *
     * @return the string representation of the parking as a 2-dimensional square. Note that cars do a U turn to continue to the next lane.
     */
    override fun toString(): String {
        return allIndexes()
            .groupBy { it / size }
            .map { if (it.key % 2 == 0) it.value else it.value.reversed() }
            .map { line -> line.map { index -> mapIndexToChar(index) } }
            .joinToString(System.lineSeparator()) { it.joinToString("") }
    }

    private fun allIndexes() = (0 until size * size)

    private fun mapIndexToChar(it: Int): Char {
        return if (pedExits.contains(it)) {
            '='
        } else if (disabledBays.contains(it) && !parkedBays.contains(it)) {
            '@'
        } else {
            parkedBays.getOrDefault(it, 'U')
        }
    }
}