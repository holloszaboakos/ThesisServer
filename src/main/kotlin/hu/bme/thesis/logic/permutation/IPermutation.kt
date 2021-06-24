package hu.bme.thesis.logic.permutation

import java.math.BigDecimal

interface IPermutation {
     var alive: Boolean
     var cost: Double
     var iteration: Int
     val size:Int
     val type:EPermutationFactory

     operator fun get(index: Int): Int
     operator fun set(index: Int, value: Int)

     fun indexOf(value: Int): Int
     fun contains(value: Int): Boolean

     fun <T>map(mapper: (value:Int) -> T) : List<T>
     fun forEach(operation: (value:Int) -> Unit)
     fun forEachIndexed(operation: (index:Int, value:Int) -> Unit)
     fun setEach(operation: (index:Int, value:Int) -> Int)

     fun <T> mapSlice(mapper: (slice:List<Int>) -> T): List<T>
     fun forEachSlice(operation: (slice:List<Int>) -> Unit)
     fun forEachSliceIndexed(operation: (index:Int, slice:List<Int>) -> Unit)
     fun slice(indices:IntRange) : List<Int>

     fun shuffle()
     fun first(selector: (value:Int) -> Boolean) : Int

     fun setData(data:Array<IntArray>)
     fun getData():Array<IntArray>
     fun copy():IPermutation

}
