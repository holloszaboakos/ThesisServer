package thesis

import thesis.data.OHibernateManager
import thesis.data.web.Test
import thesis.data.web.TestItem

fun main() {
    OHibernateManager.save(
        Test(
            id = "test",
            items = arrayOf(
                TestItem(
                    id = "testItem1",
                    value = 1
                ),
                TestItem(
                    id = "testItem2",
                    value = 1
                ),
                TestItem(
                    id = "testItem3",
                    value = 1
                ),
                TestItem(
                    id = "testItem4",
                    value = 1
                ),
                TestItem(
                    id = "testItem5",
                    value = 1
                )
            )
        )
    )
    OHibernateManager.deleteById<Test>("test")
    OHibernateManager.closeFactory()
}