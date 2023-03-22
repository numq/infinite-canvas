package canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class CanvasItem<T>(
    internal val data: T,
    internal val size: Size,
    internal val position: Offset,
)