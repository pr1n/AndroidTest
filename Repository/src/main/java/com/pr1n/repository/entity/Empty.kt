package com.pr1n.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Empty(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val type: Int
)