package com.plcoding.roomguideandroid

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val name: String="",
    val price: String="",
    val isPaid: Boolean=false,
    val dateOfPurchase: String="",
    val isAddingExpense: Boolean = false,
    val sortType: SortType = SortType.NAME
)