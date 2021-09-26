package hu.bme.thesis.logic.specimen

interface IRepresentation {
     var inUse: Boolean
     var costCalculated: Boolean
     var cost: Double
     var iteration: Int
     var orderInPopulation: Int
     val size: Int

     operator fun get(index: Int): Int
     operator fun set(index: Int, value: Int)

     fun indexOf(value: Int): Int
     fun contains(value: Int): Boolean

     fun <T>map(mapper: (value:Int) -> T): Sequence<T>
     fun forEach(operation: (value:Int) -> Unit)
     fun forEachIndexed(operation: (index:Int, value:Int) -> Unit)
     fun setEach(operation: (index:Int, value:Int) -> Int)

     fun <T> mapSlice(mapper: (slice: Sequence<Int>) -> T): Sequence<T>
     fun forEachSlice(operation: (slice: Sequence<Int>) -> Unit)
     fun forEachSliceIndexed(operation: (index: Int, slice: Sequence<Int>) -> Unit)
     fun slice(indices:IntRange): Sequence<Int>

     fun shuffle()
     fun first(selector: (value:Int) -> Boolean) : Int

     fun setData(data: Sequence<Sequence<Int>>)
     fun getData():Array<IntArray>

}
