package br.me.vitorcsouza.train.ui.presentation.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import kotlinx.coroutines.delay

@Composable
fun CreateAccount(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    var isVisible by remember { mutableStateOf(isPreview) }

    if (!isPreview) {
        LaunchedEffect(Unit) {
            delay(500)
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Don't have an account? ")
                }

                pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
                withStyle(
                    style = SpanStyle(
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Create Account")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp
                ),
                onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "SignUp",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onClick()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateAccountPreview() {
    CreateAccount()
}
