package thesis.data

import java.io.Serializable

interface ListItemKey<T> : Serializable{
    var owner: T?
    var orderInOwner: Int
}