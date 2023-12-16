import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.plcoding.roomguideandroid.ExpenseEvent
import com.plcoding.roomguideandroid.ExpenseState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseDialog(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    var calendarState = rememberSheetState()

    
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
          monthSelection = true
        ),
        selection = CalendarSelection.Date { date ->
            onEvent(ExpenseEvent.SetDateOfPurchase(date.toString()))
        }
    )

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ExpenseEvent.HideDialog)
        },
        title = { Text(text = "Add Expense") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(ExpenseEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Name")
                    }
                )
                TextField(
                    value = state.price,
                    onValueChange = { newText ->
                        if (isValidPriceInput(newText) || newText.isEmpty()) {
                            onEvent(ExpenseEvent.SetPrice(newText))
                        }
                    },
                    placeholder = {
                        Text(text = "Price")
                    }
                )
                Button(
                    onClick = {calendarState.show()}

                ) {
                    Text(text = "Date Picker")
                }
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(ExpenseEvent.SaveExpense)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}

// Extension function to check if a String is a valid price format
fun isValidPriceInput(input: String): Boolean {
    return input.isEmpty() || input.matches("^\\d+(\\.\\d{0,2})?\$".toRegex())
}
