package br.me.vitorcsouza.train.ui.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.domain.model.ExerciseType
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import br.me.vitorcsouza.train.ui.theme.Lime

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    isCheckedExercise: Boolean = false,
    exerciseName: String = "Calf Raises",
    repeat: Int = 20,
    series: Int = 4,
    exerciseType: ExerciseType = ExerciseType.Normal,
    checkButton: () -> Unit,
) {
    val (tagBackgroundColor, tagTextColor) = when (exerciseType) {
        ExerciseType.Normal -> Color(0xFFE5E7EB) to Color(0xFF4B5563)
        ExerciseType.Progression -> Color(0xFFDBEAFE) to Color(0xFF2563EB)
        ExerciseType.Drop_Set -> Color(0xFFFFEDD5) to Color(0xFFD97706)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = checkButton,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isCheckedExercise) Icons.Filled.Circle else Icons.Outlined.Circle,
                    contentDescription = "Check Exercise",
                    tint = if (!isCheckedExercise) Color.Gray.copy(alpha = 0.6f) else Lime,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (isCheckedExercise) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    fontWeight = FontWeight.Bold,
                    color = if (isCheckedExercise) DarkBlue.copy(0.6f) else DarkBlue
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$series x $repeat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCheckedExercise) Color.Gray else Color.Unspecified
                    )

                    Text(
                        text = exerciseType.name.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = tagTextColor,
                        modifier = Modifier
                            .background(tagBackgroundColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreviewUnChecked() {
    ExerciseCard(
        checkButton = {},
        isCheckedExercise = false,
        exerciseName = "Calf Raises",
        repeat = 20,
        series = 4,
        exerciseType = ExerciseType.Normal
    )
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreviewProgression() {
    ExerciseCard(
        checkButton = {},
        isCheckedExercise = false,
        exerciseName = "Leg Press",
        repeat = 12,
        series = 4,
        exerciseType = ExerciseType.Progression
    )
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreviewDropSet() {
    ExerciseCard(
        checkButton = {},
        isCheckedExercise = false,
        exerciseName = "Leg Curls",
        repeat = 15,
        series = 3,
        exerciseType = ExerciseType.Drop_Set
    )
}
