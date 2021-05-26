package thesis.model

import java.io.Serializable

interface IListItemKey<T> : Serializable{
    var owner: T?
    var orderInOwner: Int
}