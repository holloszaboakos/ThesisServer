package hu.bme.thesis.logic.specimen

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

data class DOnePartRepresentation(
    private val permutationSize: Int,
    private val permutation: IntArray,
    override var inUse: Boolean = true,
    override var costCalculated: Boolean = true,
    override var cost: Double = -1.0,
    override var iteration: Int = 0,
    override var orderInPopulation: Int = 0
) : IRepresentation {

    private val lock = ReentrantReadWriteLock()

    constructor(data: Array<IntArray>) : this(
        data.sumOf { it.size },
        data.let {
            val permutationSize = data.sumOf { it.size }
            val sum = mutableListOf<Int>()
            data.forEachIndexed { index, array ->
                sum.addAll(array.asSequence())
                sum.add((permutationSize + index))
            }
            sum.toIntArray()
        }
    )

    constructor(other: DOnePartRepresentation) : this(
        other.permutationSize,
        other.permutation,
        other.inUse,
        other.costCalculated,
        other.cost,
        other.iteration
    )

    override val size: Int
        get() = permutation.size

    override operator fun get(index: Int) = lock.read {
        permutation[index]
    }

    override operator fun set(index: Int, value: Int) = lock.write {
        permutation[index] = value
    }

    override fun indexOf(value: Int): Int = permutation.indexOf(value)

    override fun contains(value: Int): Boolean = permutation.contains(value)

    override fun <T> map(mapper: (Int) -> T): Sequence<T> = permutation.map(mapper).asSequence()

    override fun forEach(operation: (Int) -> Unit) =
        lock.read { permutation.forEach(operation) }

    override fun forEachIndexed(operation: (Int, Int) -> Unit) =
        lock.read { permutation.forEachIndexed(operation) }

    override fun setEach(operation: (Int, Int) -> Int) = lock.write {
        permutation.forEachIndexed { index: Int, value: Int -> permutation[index] = operation(index, value) }
    }

    override fun <T> mapSlice(mapper: (slice: Sequence<Int>) -> T): Sequence<T> = lock.read {
        sequence {
            var count = 0
            while (count < permutation.size) {
                yield(sequence {
                    permutation.slice(count until permutation.size)
                        .forEach {
                            count++
                            if (it < permutationSize)
                                yield(it)
                            else
                                return@forEach
                        }
                })
            }
        }.map { mapper(it) }
    }

    override fun forEachSlice(operation: (slice: Sequence<Int>) -> Unit) = lock.read {
        sequence {
            var count = 0
            while (count < permutation.size) {
                yield(sequence {
                    permutation.slice(count until permutation.size)
                        .forEach {
                            count++
                            if (it < permutationSize)
                                yield(it)
                            else
                                return@forEach
                        }
                })
            }
        }.forEach { operation(it) }
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: Sequence<Int>) -> Unit) = lock.read {
        sequence {
            var count = 0
            while (count < permutation.size) {
                yield(sequence {
                    permutation.slice(count until permutation.size)
                        .forEach {
                            count++
                            if (it < permutationSize)
                                yield(it)
                            else
                                return@forEach
                        }
                })
            }
        }.forEachIndexed { index, sequence -> operation(index,sequence) }
    }

    override fun slice(indices: IntRange): Sequence<Int> = lock.read {
        sequence {
            yieldAll(permutation.slice(indices))
        }
    }

    override fun shuffle() = lock.write { permutation.shuffle() }
    override fun first(selector: (Int) -> Boolean): Int = lock.read { permutation.first(selector) }

    override fun setData(data: Sequence<Sequence<Int>>) {
        data.let {
            val sum = mutableListOf<Int>()
            data.forEachIndexed { index, array ->
                sum.addAll(array.asSequence())
                sum.add((permutationSize + index))
            }
            sum.toIntArray()
        }
    }

    override fun getData(): Array<IntArray> {
        lock.read {
            return mapSlice { list -> list.toList().toIntArray() }.toList().toTypedArray()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DOnePartRepresentation

        if (permutationSize != other.permutationSize) return false
        if (!permutation.contentEquals(other.permutation)) return false
        if (inUse != other.inUse) return false
        if (costCalculated != other.costCalculated) return false
        if (cost != other.cost) return false
        if (iteration != other.iteration) return false
        if (lock != other.lock) return false

        return true
    }

    override fun hashCode(): Int {
        var result = permutationSize
        result = 31 * result + permutation.contentHashCode()
        result = 31 * result + inUse.hashCode()
        result = 31 * result + costCalculated.hashCode()
        result = 31 * result + cost.hashCode()
        result = 31 * result + iteration.hashCode()
        result = 31 * result + lock.hashCode()
        return result
    }
}
