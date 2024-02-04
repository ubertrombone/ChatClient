package ui.main.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.chat.ChatComponent
import kotlinx.coroutines.launch
import ui.icons.NavBackButton
import util.BottomBarSystemNavColor
import util.SoftInputMode
import util.textFieldColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatWindow(component: ChatComponent, modifier: Modifier = Modifier) {
    val chat by component.messages.collectAsState(emptyList())
    val userInput by component.userInput.subscribeAsState()
    val input by remember(userInput) { mutableStateOf(userInput) }
    var inputIsValid by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val keyboardRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(input) { inputIsValid = input.isNotBlank() }
    BottomBarSystemNavColor(colorScheme.background)
    SoftInputMode(true)

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NavBackButton { component.navBack() }
                Text(
                    text = component.friend.username.name,
                    style = typography.headlineLarge,
                    color = colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Friend Profile",
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 12.dp, top = 12.dp)
                        .size(40.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .clickable { println("TODO") }
                )
            }
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = input,
                    onValueChange = component::updateInput,
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(text = "Message") },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        imeAction = ImeAction.None,
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    modifier = Modifier
                        .weight(8f)
                        .bringIntoViewRequester(keyboardRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                scope.launch { keyboardRequester.bringIntoView() }
                            }
                        }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}