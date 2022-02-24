package hu.bme.thesis.model

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.cfg.Configuration
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object OHibernateManager {
    var sessionFactory: SessionFactory? = null
    var lock = ReentrantLock()

    fun openFactory() = lock.withLock {
        if (sessionFactory == null || sessionFactory?.isClosed == true)
            sessionFactory = Configuration().configure().buildSessionFactory()
    }

    fun closeFactory() = lock.withLock {
        sessionFactory?.close()
        sessionFactory = null
    }

    fun openSession(): Session = lock.withLock {
        openFactory()
        return sessionFactory?.openSession() ?: throw Error("Factory is closed")
    }

    fun closeSession(session: Session, transaction: Transaction) = lock.withLock {
        transaction.commit()
        session.clear()
        session.joinTransaction()
        session.close()
    }

    inline fun <reified T> find(id: String?): T = lock.withLock {
        id ?: throw Error("id should not be null")
        val session = openSession()
        val transaction = session.beginTransaction()
        val mapped = session.find(T::class.java, id)
        closeSession(session,transaction)
        return mapped
    }

    inline fun <reified T : Any> find(idList: List<String>?): List<T> = lock.withLock {
        idList ?: throw Error("id should not be null")
        var session = openSession()
        var transaction = session.beginTransaction()

        val mapped = idList.mapIndexed { index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.find(T::class.java, it)
        }
        closeSession(session,transaction)
        return mapped
    }

    inline fun <reified Item, Owner> find(IListItemKey: IListItemKey<Owner>): Item = lock.withLock {
        if (IListItemKey.owner == null || IListItemKey.orderInOwner == -1) throw Error("id should not be null")
        val session = openSession()
        val transaction = session.beginTransaction()
        val mapped = session.find(Item::class.java, IListItemKey)
        closeSession(session,transaction)
        return mapped
    }

    inline fun <reified T> create(value: T) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        session.persist(value)
        closeSession(session,transaction)
    }

    inline fun <reified T> createAll(value: List<T>) = lock.withLock {
        var session = openSession()
        var transaction = session.beginTransaction()
        value.forEachIndexed {index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.persist(it)
        }
        closeSession(session,transaction)
    }

    fun <T> save(value: T) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        session.save(value)
        closeSession(session,transaction)
    }

    fun <T> saveAll(value: List<T>) = lock.withLock {
        var session = openSession()
        var transaction = session.beginTransaction()
        value.forEachIndexed {index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.save(it)
        }
        closeSession(session,transaction)
    }


    inline fun <reified T> update(value: T) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        session.update(value)
        closeSession(session,transaction)
    }

    inline fun <reified T> updateAll(value: List<T>) = lock.withLock {
        var session = openSession()
        var transaction = session.beginTransaction()
        value.forEachIndexed {index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.update(it) }
        closeSession(session,transaction)
    }


    inline fun <reified T> saveOrUpdate(value: T) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        session.saveOrUpdate(value)
        closeSession(session,transaction)
    }

    inline fun <reified T> saveOrUpdateAll(value: List<T>) = lock.withLock {
        var session = openSession()
        var transaction = session.beginTransaction()
        value.forEachIndexed {index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.saveOrUpdate(it)
        }
        closeSession(session,transaction)
    }


    inline fun <reified T> delete(value: T) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        session.delete(value)
        closeSession(session,transaction)
    }

    inline fun <reified T> deleteAll(value: List<T>) = lock.withLock {
        var session = openSession()
        var transaction = session.beginTransaction()
        value.forEachIndexed {index, it ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            session.delete(it)
        }
        closeSession(session,transaction)
    }

    inline fun <reified T> deleteById(id: String?) = lock.withLock {
        val session = openSession()
        val transaction = session.beginTransaction()
        val value = session.load(T::class.java, id)
        session.delete(value)
        closeSession(session,transaction)
    }

    inline fun <reified T> deleteAllById(ids: List<String>?) = lock.withLock {
        ids ?: throw Error("ids should not be null")
        var session = openSession()
        var transaction = session.beginTransaction()
        ids.forEachIndexed {index, id ->
            if(index % 1000 == 999){
                closeSession(session,transaction)
                session = openSession()
                transaction = session.beginTransaction()
            }
            val value = session.load(T::class.java, id)
            session.delete(value)
        }
        closeSession(session,transaction)
    }

    inline fun <reified T>list(tableName: String?): List<T> = lock.withLock {
        tableName ?: throw Exception("table name should not be null")
        val session = openSession()
        val transaction = session.beginTransaction()
        val mapped = session
            .createNamedQuery("list$tableName", T::class.java)
            .resultList
            .map { it as T }
        closeSession(session,transaction)
        return mapped
    }

    inline fun <reified L>findByName(tableName: String?,name:String?): L = lock.withLock{
        tableName ?: throw Exception("tableName should not be null")
        name ?: throw Exception("name should not be null")
        val session = openSession()
        val transaction = session.beginTransaction()
        val mapped = session
            .createNamedQuery("findByName$tableName", L::class.java)
            .setParameter("name",name)
            .resultList
            .first()!!
        closeSession(session,transaction)
        return mapped
    }
}
