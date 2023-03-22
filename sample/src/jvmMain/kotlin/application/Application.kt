import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.singleWindowApplication
import canvas.CanvasScreen

fun main() = singleWindowApplication {
    MaterialTheme {
        CanvasScreen()
    }
}