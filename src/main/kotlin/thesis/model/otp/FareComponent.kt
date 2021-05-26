package thesis.model.otp


class FareComponent(
    var fareId: FeedScopedId,
    var price: Money,
    var routes: MutableList<FeedScopedId> = mutableListOf()
)
