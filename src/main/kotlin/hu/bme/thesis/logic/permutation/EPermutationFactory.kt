package hu.bme.thesis.logic.permutation

enum class EPermutationFactory {
    ONE_PART {
        override fun produce(values: Array<IntArray>): DOnePartRepresentation = DOnePartRepresentation(values)
    },
    TWO_PART {
        override fun produce(values: Array<IntArray>): DTwoPartRepresentation = DTwoPartRepresentation(values)
    };
    abstract fun produce(values: Array<IntArray>) : IPermutation
}