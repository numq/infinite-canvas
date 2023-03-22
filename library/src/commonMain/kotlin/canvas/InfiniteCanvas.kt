package canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> InfiniteCanvas(modifier: Modifier = Modifier, state: CanvasState<T> = rememberCanvasState()) = with(state) {
    Box(
        modifier
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures {
                    insertionPosition = it
                    onCanvasClick()
                }
            }.pointerInput(Unit) {
                detectDragGestures { change, (x, y) ->
                    change.consumeAllChanges()
                    val updatedOffset = canvasOffset + Offset(x, y)
                    if (maxCanvasOffset == Offset.Zero || (-maxCanvasOffset.x <= updatedOffset.x && updatedOffset.x <= maxCanvasOffset.x && -maxCanvasOffset.y <= updatedOffset.y && updatedOffset.y <= maxCanvasOffset.y)) {
                        canvasOffset += Offset(x, y)
                    }
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(
                if (items.size > 1) Color.White else Color.Unspecified,
                topLeft + canvasOffset - padding,
                canvasSize.copy(
                    width = canvasSize.width + (padding * 2f).x,
                    height = canvasSize.height + (padding * 2f).y
                )
            )
        }
        state.items.forEachIndexed { index, item ->
            with(item) {
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }
                Box(
                    Modifier
                        .zIndex(index.toFloat())
                        .size(size.run { DpSize(width.dp, height.dp) })
                        .graphicsLayer {
                            translationX = position.x + offsetX + canvasOffset.x
                            translationY = position.y + offsetY + canvasOffset.y
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDragCancel = {
                                offsetX = 0f
                                offsetY = 0f
                            }, onDragEnd = {
                                updatePosition(index, Offset(offsetX, offsetY))
                                offsetX = 0f
                                offsetY = 0f
                            }) { change, (x, y) ->
                                change.consumeAllChanges()
                                offsetX += x
                                offsetY += y
                            }
                        }
                ) {
                    content(item.data)
                }
            }
        }
    }
}