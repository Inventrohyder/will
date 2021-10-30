package com.inventrohyder.will

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "will_table")
data class Will(
    @PrimaryKey
    @ColumnInfo(name = "will") val will: String
)
