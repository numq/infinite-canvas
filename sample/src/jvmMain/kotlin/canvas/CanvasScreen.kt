package canvas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import image.ImageItem
import org.jetbrains.skia.Image
import upload.UploadDialog
import java.util.*

@Composable
fun CanvasScreen() {

    val (uploading, setUploading) = remember { mutableStateOf(false) }

    val canvasState = rememberCanvasState<ImageItem>(onCanvasClick = {
        setUploading(true)
    }) {
        Card(Modifier.size(64.dp, 64.dp)) {
            Image(it.bitmap, "item", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
    }

    if (uploading) {
        UploadDialog(onClose = {
            setUploading(false)
        }) { files ->
            files.mapNotNull {
                runCatching {
                    ImageItem(
                        UUID.randomUUID().toString(),
                        Image.makeFromEncoded(it.bytes).toComposeImageBitmap()
                    )
                }.getOrNull()
            }.let(canvasState::addItems)
        }
    }

    InfiniteCanvas(Modifier.fillMaxSize(), canvasState)
}