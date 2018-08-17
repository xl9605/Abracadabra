package ac.ict.humanmotion.abracadabra

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_front_camera.*
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import kotlin.concurrent.thread

class FrontCameraActivity : BaseActivity(), CameraBridgeViewBase.CvCameraViewListener2 {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CROP_REQUEST_CODE -> setUriToView()
            CAMERA_REQUEST_CODE -> clipPhoto()
        }
    }

    private fun setUriToView() {
        thread {
            val tempMat = mRgba

            runOnUiThread {
                showToast("OCR数据已上传到服务器")
                showToast("正在等待结果")
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun clipPhoto(uri: Uri = imageUri) {
        startActivityForResult(Intent("com.android.camera.action.CROP")
                .setDataAndType(uri, "image/*")
                .putExtra("crop", "true")
                .putExtra(MediaStore.EXTRA_OUTPUT, outUri)
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                .putExtra("return-data", false), CROP_REQUEST_CODE)
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        inputFrame?.rgba()?.let {
            //            selfBinary(it.nativeObjAddr, mRgba.nativeObjAddr)
            mRgba = it
        }

        return mRgba
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        println("onCameraViewStarted")
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            println("FAIL")
        } else cameraView.enableView()
    }

    override fun onPause() {
        super.onPause()
        if (cameraView != null) {
            cameraView.disableView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraView != null) {
            cameraView.disableView()
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_front_camera

    override fun init() {
        getStorageAccessPermissions()

        cameraView.setCvCameraViewListener(this)

        cameraFab.setOnClickListener {

            thread {
                val tempMat = mRgba
                runOnUiThread {
                    showToast("图像大小为${tempMat.size().height * tempMat.size().width} .数据已上传到服务器")
                }

             /*   startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        .putExtra(MediaStore.EXTRA_OUTPUT, imageUri), CAMERA_REQUEST_CODE)*/
                val intent = Intent()
                intent.setClass(this, WorkList::class.java)
                startActivityForResult(intent, 15)
            }

            showToast("正在处理")

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

    @TargetApi(23)
    private fun getStorageAccessPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), RES_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("$PERMISSION_TAG:onRequestPermissionsResult: ${grantResults[0]}")
        when (requestCode) {
            RES_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) println("$PERMISSION_TAG:permission get!") else {
                println("$PERMISSION_TAG:permission denied! ")
                finish()
            }
        }
    }

}
