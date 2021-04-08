package thesis.core.permutation

enum class EPermutationFactory{
    TWO_PART_REPRESENTATION {
        override fun invoke(data: Array<IntArray>) = TwoPartRepresentation(data)
    };
    abstract operator fun invoke(data:Array<IntArray>) : IPermutation
}