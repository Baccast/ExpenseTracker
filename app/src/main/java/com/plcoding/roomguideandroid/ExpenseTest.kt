package com.plcoding.roomguideandroid

import org.junit.Assert.*
import org.junit.Test

class ExpenseTest {
    @Test
    fun `testExpenseName`() {
        val expense = Expense(name = "Test", price = "100.0", isPaid = false, dateOfPurchase = "12/15/2023")
        assertEquals("Test", expense.name)
    }

    @Test
    fun `testExpenseDate`() {
        val expense = Expense(name = "Test", price = "100.0", isPaid = false, dateOfPurchase = "12/15/2023")
        assertEquals("12/15/2023", expense.dateOfPurchase)
    }

    @Test
    fun `testExpenseIsPaidStatus`() {
        val expense = Expense(name = "Test", price = "100.0", isPaid = false, dateOfPurchase = "12/15/2023")
        assertFalse(expense.isPaid)
    }
}