package ac.ict.humanmotion.abracadabra

import ac.ict.humanmotion.abracadabra.Bean.Operation
import ac.ict.humanmotion.abracadabra.Fragment.CompareFragment
import ac.ict.humanmotion.abracadabra.Fragment.StartOperateFragment
import ac.ict.humanmotion.abracadabra.HTTPAround.MyTemplateObserver
import ac.ict.humanmotion.abracadabra.Lpms.ConnectionFragment
import ac.ict.humanmotion.abracadabra.Lpms.ImuStatus
import ac.ict.humanmotion.abracadabra.Lpms.LpmsBData
import ac.ict.humanmotion.abracadabra.Lpms.LpmsBThread
import ac.ict.humanmotion.abracadabra.OCR.OCRActivity
import android.Manifest
import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.concurrent.thread

class MainActivity : BaseActivity(), ConnectionFragment.OnConnectListener {
    object global{
        var connectFlag:Int = 0
    }
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun init() {
        getStorageAccessPermissions()

        initLpms()
        initRxJava()
        initSimple()


        compareFragment = CompareFragment()

        connectionFragment = ConnectionFragment()
        startOperateFragment = StartOperateFragment()

        initCompareFragment()

    }

    private fun initCompareFragment() {

        // LOAD FIRST
        supportFragmentManager.beginTransaction().add(R.id.container, compareFragment).commit()

        // (IF FACE OK) THEN (REMOVE FIRST)
        supportFragmentManager.beginTransaction().remove(compareFragment).commit()

        // LOAD SECOND FIRST
        supportFragmentManager.beginTransaction().add(R.id.container, connectionFragment).commit()

    }

    // retrofit
    private fun initSimple() {
        cloudAPI.getOperation(offset = 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Operation>>() {
                    override fun onNext(t: List<Operation>) {
                        println(t.toString())
                    }
                })
    }

    private fun initLpms() {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        thread {
            while (!stopPollThread) {
                synchronized(lpmsList) {
                    for (e in lpmsList) {
                        var d: LpmsBData
                        while (e.hasNewData()) {
                            d = e.lpmsBData!!
                            if (lpmsB.address.equals(e.address))
                                imuData = LpmsBData(d)
                        }
                    }
                }
            }
        }
    }

    @TargetApi(23)
    private fun getStorageAccessPermissions() {
        requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION), OCRActivity.RES_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("${OCRActivity.PERMISSION_TAG}:onRequestPermissionsResult: ${grantResults[0]}")
        when (requestCode) {
            OCRActivity.RES_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) println("${OCRActivity.PERMISSION_TAG}:permission get!") else {
                println("${OCRActivity.PERMISSION_TAG}:permission denied! ")
                finish()
            }
        }
    }

    lateinit var connectionFragment: ConnectionFragment
    lateinit var compareFragment: CompareFragment
    lateinit var startOperateFragment: StartOperateFragment

    override fun onBackPressed() {
        //back->home
        val i = Intent(Intent.ACTION_MAIN)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
    }

    // C++ C++ C++ C++ C++ C++ C++

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        const val FRAGMENTTAG = "ConnectionFragment"

//        var globalData = LpmsBData()
    }

    //LMPS==========================================>

    lateinit var mTimer: Timer
    lateinit var lpmsB: LpmsBThread
    lateinit var btAdapter: BluetoothAdapter

    var isLpmsBConnected = false
    var imuStatus = ImuStatus()
    var handler = Handler()
    var updateFragmentsHandler = Handler()
    var imuData = LpmsBData()

    private val updateRate = 25

    val lpmsList: MutableList<LpmsBThread> = ArrayList()

    var stopPollThread = false

    private val mUpdateFragmentsTask = object : Runnable {
        override fun run() {
            synchronized(imuData) {
                updateFragment(imuData, imuStatus)
//                globalData = imuData
            }
            updateFragmentsHandler.postDelayed(this, updateRate.toLong())
        }
    }

    override fun onDestroy() {
        stopPollThread = true

        synchronized(lpmsList) {
            for (e in lpmsList) {
                e.close()
            }
        }

        super.onDestroy()
    }

    override fun onStart() {
        mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { }
            }
        }, 25, 25)

        super.onStart()
    }

    override fun onStop() {
        mTimer.cancel()

        super.onStop()
    }

    override fun onResume() {
        startUpdateFragments()

        super.onResume()
    }

    override fun onPause() {
        stopUpdateFragments()

        super.onPause()
    }


    fun startUpdateFragments() {
        updateFragmentsHandler.removeCallbacks(mUpdateFragmentsTask)
        updateFragmentsHandler.postDelayed(mUpdateFragmentsTask, 100)
    }

    fun updateFragment(d: LpmsBData, s: ImuStatus) {
        connectionFragment.updateView(d, s)
    }

    fun stopUpdateFragments() {
        updateFragmentsHandler.removeCallbacks(mUpdateFragmentsTask)
    }

//    inner class DataAnalysisThread : Runnable {
//        override fun run() {
//            Log.e("Main", "New Data")
//
//        }
//    }

    override fun onConnect(address: String) {
        var id = 0

        synchronized(lpmsList) {
            for (aLpmsList in lpmsList) {
                if (address == aLpmsList.address) {
                    Toast.makeText(baseContext, "$address 手环设备已在线.", Toast.LENGTH_SHORT).show()
                    return
                }
                id++
            }

            lpmsB = LpmsBThread(btAdapter)

            lpmsB.setAcquisitionParameters(true, true, false, false, false, true, false)
            if (lpmsB.connect(address, id)) {
                lpmsList.add(lpmsB)

                isLpmsBConnected = true
                imuStatus.measurementStarted = true

                Toast.makeText(baseContext, "已连接手环设备 $address", Toast.LENGTH_SHORT).show()
                global.connectFlag = 1
                connectionFragment.confirmConnected(lpmsB.device)
              //  var intent=Intent()
                val intent = Intent()
                intent.setClass(this!!, StartOperateFragment::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(baseContext, "连接手环设备 $address 失败.请重新连接.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }


    fun onSensorSelectionChanged(address: String) {
        synchronized(lpmsList) {
            for (e in lpmsList) {
                if (address == e.address) {
                    lpmsB = e
                    Log.e("lpms", "[LpmsBMainActivity] In main activity: " + lpmsB.address)
                    return
                }
            }
        }
    }

    override fun onDisconnect() {
        synchronized(lpmsList) {
            for (e in lpmsList) {
                if (lpmsB.address.equals(e.address)) {
                    Toast.makeText(baseContext, "已断开连接 " + e.address, Toast.LENGTH_SHORT).show()
                    e.close()
                    lpmsList.remove(e)
                    if (lpmsList.size == 0) imuStatus.measurementStarted = true
                    return
                }
            }
        }
    }
}
