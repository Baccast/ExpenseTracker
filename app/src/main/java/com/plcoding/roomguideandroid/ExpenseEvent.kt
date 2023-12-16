package com.plcoding.roomguideandroid

sealed interface ExpenseEvent {
    object SaveExpense : ExpenseEvent
    data class SetName(val name: String) : ExpenseEvent
    data class SetDateOfPurchase(val dateOfPurchase: String) : ExpenseEvent
    data class SetPrice(val price: String) : ExpenseEvent
    data class SetIsPaid(val isPaid: Boolean) : ExpenseEvent
    object ShowDialog : ExpenseEvent
    object HideDialog : ExpenseEvent
    data class SortExpenses(val sortType: SortType) : ExpenseEvent
    data class DeleteExpense(val expense: Expense) : ExpenseEvent
    object NavigateToHome : ExpenseEvent
    object NavigateToExpenseTracker : ExpenseEvent
    data class UpdateExpense(val expense: Expense): ExpenseEvent
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ExpenseTracker : Screen("expense")

}
