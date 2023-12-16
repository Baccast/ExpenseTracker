package com.plcoding.roomguideandroid

import ExpenseScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.plcoding.roomguideandroid.ui.theme.RoomGuideAndroidTheme
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ExpenseDatabase::class.java,
            "expenses.db"
        ).build()
    }

    private val viewModel by viewModels<ExpenseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return ExpenseViewModel(db.dao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomGuideAndroidTheme {
                val state by viewModel.state.collectAsState()
                val navController = rememberNavController()

                NavHost(navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        ExpenseScreen(state = state, onEvent = viewModel::onEvent)
                    }
                    composable(Screen.ExpenseTracker.route) {
                        HomeScreen(onEvent = viewModel::onEvent)
                    }
                }

                lifecycleScope.launchWhenStarted {
                    viewModel.navigationEvents.collect { event ->
                        when(event) {
                            is ExpenseEvent.NavigateToHome -> navController.navigate(Screen.Home.route)
                            is ExpenseEvent.NavigateToExpenseTracker -> navController.navigate(Screen.ExpenseTracker.route)
                            else -> {} // Do nothing for all other events
                        }
                    }
                }
            }
        }
    }
}