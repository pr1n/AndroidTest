package com.apesmedical.commonsdk.base

import com.apesmedical.commonsdk.db.EmptyDao
import com.apesmedical.commonsdk.db.MainDB
import org.koin.java.KoinJavaComponent.get

interface LocalService {
    val emptyDao: EmptyDao

    companion object : LocalService {
        private val mainDB: MainDB = get(MainDB::class.java)
        override val emptyDao: EmptyDao get() = mainDB.emptyDao()
    }
}