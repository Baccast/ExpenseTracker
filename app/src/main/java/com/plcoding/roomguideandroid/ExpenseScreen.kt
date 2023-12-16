import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.plcoding.roomguideandroid.ExpenseEvent
import com.plcoding.roomguideandroid.ExpenseState
import com.plcoding.roomguideandroid.SortType


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(ExpenseEvent.NavigateToExpenseTracker)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(ExpenseEvent.NavigateToHome)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Expense Tracker"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ExpenseEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add expense"
                )
            }
        },
    ) { paddingValues ->
        if(state.isAddingExpense) {
            AddExpenseDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(ExpenseEvent.SortExpenses(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(ExpenseEvent.SortExpenses(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.expenses) { expense ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (expense.isPaid){
                            val context = LocalContext.current
                            val openURL = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
                                // Handle the result if needed
                            }
                            Text(
                                text = "${expense.name} $${expense.price}",
                                fontSize = 28.sp,
                                color = Color.Green,
                                modifier = Modifier.clickable {
                                    val url = "https://www.google.com/search?q=${expense.name}"
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse(url)
                                    openURL.launch(intent)
                                }

                            )
                            Text(text = expense.dateOfPurchase, fontSize = 20.sp)
                        } else {
                            Text(
                                text = "${expense.name} $${expense.price}",
                                fontSize = 28.sp,
                                color = Color.Red
                            )
                            Text(text = expense.dateOfPurchase, fontSize = 20.sp)
                        }
                    }
                    IconButton(onClick = {
                        onEvent(ExpenseEvent.DeleteExpense(expense))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete contact"
                        )
                    }
                    if (expense.isPaid){
                        IconButton(onClick = {
                            onEvent(ExpenseEvent.UpdateExpense(expense))
                            onEvent(ExpenseEvent.NavigateToHome)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Mark as unpaid"
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            onEvent(ExpenseEvent.UpdateExpense(expense))
                            onEvent(ExpenseEvent.NavigateToHome)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Mark as paid"
                            )
                        }
                    }
                }
            }
            item {
                val totalBalance = state.expenses.filter { !it.isPaid }.fold(0.0) { acc, expense ->
                    acc + expense.price.toDouble()
                }
                val totalBalanceFormatted = String.format("%.2f", totalBalance)
                Text(
                    text = "Total Balance: $totalBalanceFormatted",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 23.sp,
                    color = Color.Red
                )
            }
            item {
                val totalExpenses = state.expenses.fold(0.0) { acc, expense ->
                    acc + expense.price.toDouble()
                }
                val totalExpensesFormatted = String.format("%.2f", totalExpenses)
                Text(
                    text = "Total Expenses: $totalExpensesFormatted",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 23.sp,
                    color = Color.Black
                )
            }
        }
    }
}
