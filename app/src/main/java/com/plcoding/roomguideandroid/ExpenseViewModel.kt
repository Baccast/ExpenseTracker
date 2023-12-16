package com.plcoding.roomguideandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(
    private val dao: ExpenseDao
): ViewModel() {
    val navigationEvents = MutableSharedFlow<ExpenseEvent>()
    private val _sortType = MutableStateFlow(SortType.NAME)
    private val _expenses = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.DATE -> dao.getExpensesOrderedByDate()
                SortType.NAME -> dao.getExpensesOrderedByName()
                SortType.STATUS -> dao.getExpensesOrderedByIsPaid()
                SortType.PRICE -> dao.getExpensesOrderedByPrice()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ExpenseState())
    val state = combine(_state, _sortType, _expenses) { state, sortType, expenses ->
        state.copy(
            expenses = expenses,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())

    fun onEvent(event: ExpenseEvent) {
        when(event) {
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }
            ExpenseEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingExpense = false
                )}
            }
            ExpenseEvent.SaveExpense -> {
                val name = state.value.name
                val price = state.value.price
                val isPaid = state.value.isPaid
                val dateOfPurchase = state.value.dateOfPurchase

                if(name.isBlank() || dateOfPurchase.isBlank() || price.isBlank()){
                    return
                }

                val expense = Expense(
                    name = name,
                    price = price,
                    dateOfPurchase = dateOfPurchase,
                    isPaid = isPaid
                )
                viewModelScope.launch {
                    dao.upsertExpense(expense)
                }
                _state.update { it.copy(
                    isAddingExpense = false,
                    name = "",
                    price = "",
                    isPaid = false,
                    dateOfPurchase = ""
                ) }
            }
            is ExpenseEvent.UpdateExpense -> {
                var updatedExpense = event.expense
                updatedExpense.isPaid = !updatedExpense.isPaid
                viewModelScope.launch {
                    dao.upsertExpense(updatedExpense)
                }
            }
            is ExpenseEvent.SetDateOfPurchase -> {
                _state.update { it.copy(
                    dateOfPurchase = event.dateOfPurchase
                ) }
            }
            is ExpenseEvent.SetIsPaid -> {
                _state.update { it.copy(
                    isPaid = event.isPaid
                ) }
            }
            is ExpenseEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is ExpenseEvent.SetPrice ->
                _state.update { it.copy(
                price = event.price
            ) }
            ExpenseEvent.ShowDialog ->
                _state.update { it.copy(
                isAddingExpense = true
            ) }
            is ExpenseEvent.SortExpenses -> {
                _sortType.value = event.sortType
            }
            is ExpenseEvent.NavigateToHome -> viewModelScope.launch { navigationEvents.emit(ExpenseEvent.NavigateToHome) }
            is ExpenseEvent.NavigateToExpenseTracker -> viewModelScope.launch { navigationEvents.emit(ExpenseEvent.NavigateToExpenseTracker) }
        }


    }




}