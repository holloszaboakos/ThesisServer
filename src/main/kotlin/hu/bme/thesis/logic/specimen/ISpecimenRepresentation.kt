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
     val permutationSize: Int

     operator fun get(index: Int): Int
     operator fun set(index: Int, value: Int)

     fun indexOf(value: Int): Int
     fun contains(value: Int): Boolean

     fun <T>map(mapper: (value:Int) -> T): Flow<T>
     fun forEach(operation: (value:Int) -> Unit)
     fun forEachIndexed(operation: (index:Int, value:Int) -> Unit)
     fun setEach(operation: (index:Int, value:Int) -> Int)

     fun <T> mapSlice(mapper: (slice: Flow<Int>) -> T): Flow<T>
     fun forEachSlice(operation: (slice: Flow<Int>) -> Unit)
     fun forEachSliceIndexed(operation: (index: Int, slice: Flow<Int>) -> Unit)
     fun slice(indices:IntRange): Flow<Int>

     fun shuffle()
     fun first(selector: (value:Int) -> Boolean) : Int

     suspend fun setData(data: Flow<Flow<Int>>)
     fun getData():Flow<Flow<Int>>

     fun checkFormat():Boolean

}
