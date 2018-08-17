package ac.ict.humanmotion.abracadabra

import ac.ict.humanmotion.abracadabra.tools.CircularImage
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import org.opencv.core.Mat
import kotlin.concurrent.thread

class WorkList : BaseActivity(){
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CROP_REQUEST_CODE -> setUriToView()
            CAMERA_REQUEST_CODE -> clipPhoto()
        }
    }

    private fun setUriToView() {
        thread {
            //val tempMat = mRgba

            runOnUiThread {
                showToast("OCR数据已上传到服务器")
                showToast("正在等待结果")
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun clipPhoto(uri: Uri = WorkList.imageUri) {
        startActivityForResult(Intent("com.android.camera.action.CROP")
                .setDataAndType(uri, "image/*")
                .putExtra("crop", "true")
                .putExtra(MediaStore.EXTRA_OUTPUT, WorkList.outUri)
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                .putExtra("return-data", false), WorkList.CROP_REQUEST_CODE)
    }

    override val layoutId: Int
        get() = R.layout.work_input

    override fun init() {

       // cameraView.setCvCameraViewListener(this)
        val plogin = findViewById<View>(R.id.cover_user_photo) as ImageView
        plogin.setOnClickListener {
            // TODO Auto-generated method stub
           // login()
           // val imageUri = Uri.parse("file:///storage/emulated/0/tessdata/temp.jpg")
         //   const val CAMERA_REQUEST_CODE = 10086
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, imageUri), CAMERA_REQUEST_CODE)
            showToast("正在处理")
            /* val intent = Intent()
             intent.setClass(this, WorkListInput::class.java)
             startActivityForResult(intent, 15)*/
        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        const val PERMISSION_TAG = "RequestPermissions"
        const val CAMERA_REQUEST_CODE = 10086
        const val CROP_REQUEST_CODE = 10085
        const val RES_REQUEST_CODE = 10000
        const val rex = "/*-+)(<>'\\~!@$%&^ -:;[]{}「『…【】_《》oo′\"`\'“”‘’,."
        val outUri = Uri.parse("file:///storage/emulated/0/tessdata/output.jpg")

        val imageUri = Uri.parse("file:///storage/emulated/0/tessdata/temp.jpg")
    }


    private lateinit var mRgba: Mat

    private var nowIsOcrTime = false

    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}