package br.me.vitorcsouza.train.ui.presentation.edit_workout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.ExerciseType
import br.me.vitorcsouza.train.ui.components.TextFieldCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditCard(
    exercise: Exercise,
    onNameChange: (String) -> Unit,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onTypeChange: (ExerciseType) -> Unit,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Variável para acumular a distância do arrasto
    var dragAmountAccumulated by remember { mutableFloatStateOf(0f) }
    val dragThreshold = 80f // Distância necessária para trocar de posição

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F5)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = "Arrastar para reordenar",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragAmountAccumulated += dragAmount.y
                                
                                if (dragAmountAccumulated > dragThreshold) {
                                    onMoveDown()
                                    dragAmountAccumulated = 0f
                                } else if (dragAmountAccumulated < -dragThreshold) {
                                    onMoveUp()
                                    dragAmountAccumulated = 0f
                                }
                            },
                            onDragEnd = { dragAmountAccumulated = 0f },
                            onDragCancel = { dragAmountAccumulated = 0f }
                        )
                    }
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextFieldCustom(
                        value = exercise.name,
                        onValueChange = onNameChange,
                        placeholder = "Exercise Name",
                        icon = null,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remover Exercício",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable { onRemove() }
                            .padding(4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextFieldCustom(
                        value = if (exercise.sets == 0) "" else exercise.sets.toString(),
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                onSetsChange(newValue)
                            }
                        },
                        placeholder = "Sets",
                        icon = null,
                        modifier = Modifier.weight(1f)
                    )

                    TextFieldCustom(
                        value = if (exercise.reps == 0) "" else exercise.reps.toString(),
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                onRepsChange(newValue)
                            }
                        },
                        placeholder = "Reps",
                        modifier = Modifier.weight(1f),
                        icon = null,
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1.8f)
                    ) {
                        TextFieldCustom(
                            value = exercise.type.name.replace("_", " "),
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Type",
                            icon = null,
                            modifier = Modifier.menuAnchor(),
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            ExerciseType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name.replace("_", " ")) },
                                    onClick = {
                                        onTypeChange(type)
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseEditCardPreview() {
    val mockExercise = Exercise(
        id = "1",
        name = "Squats",
        sets = 4,
        reps = 10,
        type = ExerciseType.Normal
    )

    Column(modifier = Modifier.padding(16.dp)) {
        ExerciseEditCard(
            exercise = mockExercise,
            onNameChange = {},
            onSetsChange = {},
            onRepsChange = {},
            onTypeChange = {},
            onRemove = {}
        )
    }
}
