package com.pr1n.repository.local

import com.pr1n.repository.local.dao.EmptyDao
import com.pr1n.repository.local.db.MainDB
import org.koin.java.KoinJavaComponent.inject

interface LocalService {
    val emptyDao: EmptyDao

    companion object : LocalService {
        private val mainDB: MainDB by inject(MainDB::class.java)
        override val emptyDao: EmptyDao get() = mainDB.emptyDao()
    }
}