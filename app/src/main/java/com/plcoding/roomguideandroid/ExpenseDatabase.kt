package com.plcoding.roomguideandroid

import androidx.room.Database
import androidx.room.RoomDatabase

// BingChat
@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract val dao: ExpenseDao
}