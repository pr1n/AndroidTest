package com.apesmedical.commonsdk.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

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

@Entity
data class Empty(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val type: Int
)