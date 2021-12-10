package com.inventrohyder.will

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "will_table")
data class Will(
    @ColumnInfo(name = "will") val will: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0 //last so that we don't have to pass an ID value or named arguments
)