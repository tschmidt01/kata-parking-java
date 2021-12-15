package victor.kata.parking

/**
 * Builder class to get a parking instance
 */
class ParkingBuilder {

    private var size: Int? = null
    private val pedestrianExits: MutableSet<Int> = mutableSetOf()
    private val disabledBays: MutableSet<Int> = mutableSetOf()

    fun withSquareSize(size: Int): ParkingBuilder {
        this.size = size
        return this
    }

    fun withPedestrianExit(pedestrianExitIndex: Int): ParkingBuilder {
        pedestrianExits += pedestrianExitIndex
        return this
    }

    fun withDisabledBay(disabledBayIndex: Int): ParkingBuilder {
        disabledBays += disabledBayIndex
        return this
    }

    fun build(): Parking {
        return Parking(requireNotNull(size), pedestrianExits, disabledBays)
    }
}