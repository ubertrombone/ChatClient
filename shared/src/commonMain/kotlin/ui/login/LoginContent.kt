package ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.login.LoginComponent
import util.MainPhases
import util.MainPhases.REGISTER
import util.ShapeTokens
import util.textFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val status by component.status.subscribeAsState()
    val username by component.username.subscribeAsState()
    var usernameInput by remember(username) { mutableStateOf(username) }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(status) {
        component.server.login()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = component.title,
                        fontSize = typography.headlineLarge.fontSize,
                        fontWeight = typography.headlineLarge.fontWeight
                    )
                },
                actions = {
                    Text(
                        text = "Register",
                        fontSize = typography.bodyMedium.fontSize,
                        fontWeight = typography.bodyMedium.fontWeight,
                        modifier = Modifier.clickable { component.pushTo(REGISTER) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
             OutlinedTextField(
                 value = usernameInput,
                 onValueChange = {
                    usernameInput = it
                    component.update(it)
                 },
                 label = { Text(text = "Username", fontSize = typography.labelMedium.fontSize) },
                 leadingIcon = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Username") },
                 colors = textFieldColors(),
                 shape = RoundedCornerShape(ShapeTokens.roundedCorners),
                 singleLine = true,
                 modifier = Modifier.width(200.dp).height(200.dp),
                 // TODO: onError
             )
        }
    }
}