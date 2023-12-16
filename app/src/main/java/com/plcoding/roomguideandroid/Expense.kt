package com.plcoding.roomguideandroid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    val name: String,
    val dateOfPurchase: String,
    val price: String,
    var isPaid: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
