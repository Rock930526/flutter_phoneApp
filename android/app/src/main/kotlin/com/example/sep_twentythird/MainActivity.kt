// ...existing code...
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.provider.MediaStore
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.graphics.Bitmap // ← 新增這一行
import java.io.File

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.save_image"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "saveImage") {
                val path = call.argument<String>("path")
                if (path != null) {
                    val file = File(path)
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }
                    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    uri?.let {
                        contentResolver.openOutputStream(it)?.use { out ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        }
                        result.success(true)
                    } ?: result.error("UNAVAILABLE", "儲存失敗", null)
                } else {
                    result.error("INVALID_PATH", "路徑錯誤", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }
}