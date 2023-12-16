package com.plcoding.roomguideandroid

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM Expense ORDER BY name ASC")
    fun getExpensesOrderedByName(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense ORDER BY dateOfPurchase ASC")
    fun getExpensesOrderedByDate(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense ORDER BY CAST(price AS REAL) ASC")
    fun getExpensesOrderedByPrice(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense ORDER BY isPaid ASC")
    fun getExpensesOrderedByIsPaid(): Flow<List<Expense>>
}