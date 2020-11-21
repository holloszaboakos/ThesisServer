package thesis.data.otp


class P2<E>(first: E, second: E) : T2<E, E>(first, second) {
    constructor(entries: Array<E>) : this(entries[0], entries[1]) {
        require(entries.size == 2) { "This only takes arrays of 2 arguments" }
    }
}
