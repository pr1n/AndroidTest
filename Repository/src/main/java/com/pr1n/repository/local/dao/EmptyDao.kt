package com.pr1n.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.pr1n.repository.entity.Empty

@Dao
interface EmptyDao {

    @Insert(onConflict = REPLACE)
    fun testInsert(empty: Empty)

    @Update
    fun testUpdate(empty: Empty): Int

    @Delete
    fun testDelete(empty: Empty)

    @Query("SELECT * FROM empty WHERE id = :id")
    fun testQuery(id: Long): Empty?

}