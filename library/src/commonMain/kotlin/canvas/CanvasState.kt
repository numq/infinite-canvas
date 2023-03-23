package canvas

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun <T> rememberCanvasState(
    itemSize: Size = Size(64f, 64f),
    backgroundColor: Color = Color.LightGray,
    canvasColor: Color = Color.White,
    canvasPadding: Offset = Offset.Zero,
    maxCanvasOffset: Offset = Offset.Zero,
    onCanvasClick: () -> Unit = {},
    content: @Composable (T) -> Unit = {},
) = remember {
    CanvasState(
        itemSize,
        backgroundColor,
        canvasColor,
        canvasPadding,
        maxCanvasOffset,
        onCanvasClick,
        content
    )
}

class CanvasState<T> constructor(
    private val itemSize: Size,
    val backgroundColor: Color,
    val canvasColor: Color,
    val canvasPadding: Offset,
    val maxCanvasOffset: Offset,
    val onCanvasClick: () -> Unit,
    val content: @Composable (T) -> Unit,
) {

    internal val items = mutableStateListOf<CanvasItem<T>>()

    internal var canvasOffset by mutableStateOf(Offset.Zero)

    internal val topLeft: Offset by derivedStateOf {
        Offset(
            items.minOfOrNull { it.position.x } ?: 0f,
            items.minOfOrNull { it.position.y } ?: 0f
        )
    }

    private val bottomRight by derivedStateOf {
        Offset(
            items.maxOfOrNull { it.position.x + itemSize.width } ?: 0f,
            items.maxOfOrNull { it.position.y + itemSize.height } ?: 0f
        )
    }

    val canvasSize: Size by derivedStateOf {
        if (arrayOf(topLeft, bottomRight).all { it != Offset.Zero }) Size(
            bottomRight.x - topLeft.x,
            bottomRight.y - topLeft.y
        ) else Size.Zero
    }

    internal var insertionPosition by mutableStateOf(Offset.Zero)

    fun addItems(data: Collection<T>) {
        items.addAll(data.mapNotNull { CanvasItem(it, itemSize, insertionPosition) })
        insertionPosition = Offset.Zero
    }

    internal fun updateItemPosition(index: Int, offset: Offset) {
        items[index] = items[index].copy(position = items[index].position + offset)
    }

    internal fun removeItem(index: Int) = items.removeAt(index)
}