package ui.main.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.chat.ChatComponent
import kotlinx.coroutines.launch
import ui.composables.expect.ScrollLazyColumn
import ui.icons.NavBackButton
import util.BottomBarSystemNavColor
import util.ShapeTokens
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
    LaunchedEffect(chat) { println("MESSAGES: $chat") }
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
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(8f)
                        .bringIntoViewRequester(keyboardRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                scope.launch { keyboardRequester.bringIntoView() }
                            }
                        }
                        .onPreviewKeyEvent {
                            if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
                                component.send(input.trimEnd())
                                component.clearInput()
                                true
                            } else false
                        },
                    value = input,
                    onValueChange = component::updateInput,
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(ShapeTokens.roundedCorners),
                    placeholder = { Text(text = "Message") },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        imeAction = ImeAction.None,
                        capitalization = KeyboardCapitalization.Sentences,
                    )
                )

                FilledIconButton(
                    modifier = Modifier
                        .weight(2f)
                        .padding(horizontal = 12.dp)
                        .padding(5.dp),
                    onClick = {
                        component.send(input)
                        component.clearInput()
                    },
                    enabled = inputIsValid,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer,
                        disabledContainerColor = colorScheme.secondaryContainer,
                        disabledContentColor = colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message")
                }
            }
        }
    ) { padding ->
        ScrollLazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(items = chat.sortedBy { it.timestamp }) {
                // TODO: Row
                //  1. Aligned left if from friend, right if user's own message
                //  2. Different colors
                //  3. Possibility to have username (for groups) or not
                //  4. Space for error messages
                //  5. Icon for read receipts (JIC)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it.sender == it.primaryUserRef) {
                        Spacer(modifier = Modifier.fillMaxHeight().weight(2f))
                        Box(
                            modifier = Modifier.fillMaxHeight().weight(8f).background(colorScheme.primaryContainer),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = it.message,
                                style = typography.bodyLarge,
                                color = colorScheme.onPrimaryContainer
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxHeight().weight(8f).background(colorScheme.secondaryContainer),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = it.message,
                                style = typography.bodyLarge,
                                color = colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.fillMaxHeight().weight(2f))
                    }
                }
            }
        }
    }
}