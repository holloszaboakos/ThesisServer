package hu.bme.thesis.logic.linkernighan

/**
 * This class is meant for representing the edges, it allows to store
 * the endpoints ids and compare the edges
 */
data class Edge(val a: Int, val b: Int) {
    /*
     * Instance variables
     */
    // The first node
    private val endPoint1: Int = if (a > b) a else b

    // The second node
    private val endPoint2: Int = if (a > b) b else a

    /**
     * Getter that returns the first endpoint id
     * @param None
     * @return the first endpoint id
     */
    fun get1(): Int {
        return endPoint1
    }

    /**
     * Getter that returns the second endpoint id
     * @param None
     * @return the second endpoint id
     */
    fun get2(): Int {
        return endPoint2
    }

    /**
     * Method that compares two edges, here to make this class Comparable
     * @see java.util.Comparable
     *
     * @param Edge the edge that is going to be compared against this one
     * @return int will return -1 if less, 0 if equal, and 1 if greater
     */
    operator fun compareTo(e2: Edge): Int {
        return if (get1() < e2.get1() || get1() == e2.get1() && get2() < e2.get2()) {
            -1
        } else if (this.equals(e2)) {
            0
        } else {
            1
        }
    }

    override fun toString(): String {
        return "($endPoint1, $endPoint2)"
    }

}