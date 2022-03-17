package com.suitcore.data.local

import com.suitcore.data.model.User
import io.realm.*

/**
 * Created by dodydmw19 on 7/25/18.
 */

class RealmHelper<T : RealmObject> {

    fun saveObject(data: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r -> r.copyToRealmOrUpdate(data) }
        } finally {
            realm?.close()
        }
    }

    fun saveList(data: List<T>?) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r -> r.copyToRealmOrUpdate(data) }
        } finally {
            realm?.close()
        }
    }

    fun getData(id: Int, paramName: String, data: T): T? {
        val realm: Realm = Realm.getDefaultInstance()
        val cache: T? = realm.where(data::class.java).equalTo(paramName, id).findFirst()
        var validData: T? = null
        if (cache != null && cache.isValid) {
            validData = realm.copyFromRealm(cache)
        }
        return validData
    }

    fun getData(data: Class<T>, distinctField: String): RealmResults<T>? {
        val realm: Realm = Realm.getDefaultInstance()
        val cache: RealmResults<T>? = realm.where(data).distinct(distinctField).findAll()
        var validData: RealmResults<T>? = null
        if(cache != null && cache.isValid){
            validData = cache
        }
        return validData
    }

    fun getDataSorted(data: T): List<T>? {
        val realm: Realm = Realm.getDefaultInstance()
        val cache: RealmResults<out T>? = realm.where(data::class.java).sort("date", Sort.DESCENDING).findAll()
        var validData: List<T>? = emptyList()
        if (cache != null && cache.isValid) {
            validData = realm.copyFromRealm(cache)
        }
        return validData
    }

    fun getSingleData(data: T): T? {
        val realm: Realm = Realm.getDefaultInstance()
        val cache: T? = realm.where(data::class.java).findFirst()
        var validData: T? = null
        if (cache != null && cache.isValid) {
            validData = realm.copyFromRealm(cache)
        }
        return validData
    }

    fun getDataListQuery(id: Int, paramName: String, data: T): List<T>? {
        val realm: Realm = Realm.getDefaultInstance()
        val cache: RealmResults<out T>? = realm.where(data::class.java).equalTo(paramName, id).findAll()
        var validData: List<T>? = null
        if(cache != null && cache.isValid){
            validData = realm.copyFromRealm(cache)
        }
        return validData
    }

    fun deleteData(data: T) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(data::class.java).findAll().deleteAllFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun removeAllData() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.use { r ->
            r.executeTransaction { rr ->
                rr.deleteAll()
            }
        }
    }

}