package hu.bme.thesis.logic.specimen

import kotlinx.coroutines.flow.Flow

interface ISpecimenRepresentation {
     var inUse: Boolean
     var costCalculated: Boolean
     var cost: Double
     var iteration: Int
     var orderInPopulation: Int
     val objectiveCount: Int
     val salesmanCount: Int
     val permutationIndices: IntRange
     val permutationSize: Int

     operator fun get(index: Int): Int
     operator fun set(index: Int, value: Int)

     fun indexOf(value: Int): Int
     fun contains(value: Int): Boolean

     fun <T>map(mapper: (value:Int) -> T): Flow<T>
     fun forEach(operation: (value:Int) -> Unit)
     fun forEachIndexed(operation: (index:Int, value:Int) -> Unit)
     fun setEach(operation: (index:Int, value:Int) -> Int)

     fun <T> mapSlice(mapper: (slice: IntArray) -> T): Collection<T>
     fun forEachSlice(operation: (slice: IntArray) -> Unit)
     fun forEachSliceIndexed(operation: (index: Int, slice: IntArray) -> Unit)
     fun slice(indices:IntRange): Collection<Int>

     fun shuffle()
     fun first(selector: (value:Int) -> Boolean) : Int

     fun setData(data: Collection<IntArray>)
     fun getData(): Collection<IntArray>

     fun checkFormat():Boolean

     fun inverseOfPermutation():IntArray
     fun sequentialOfPermutation():IntArray
     fun copyOfPermutation():IntArray
     fun <T:(Int,(Int)->Int)->Collection<Int>> copyOfPermutationBy(initializer:T):Collection<Int>

}
