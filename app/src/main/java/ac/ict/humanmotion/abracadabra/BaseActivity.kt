package ac.ict.humanmotion.abracadabra

import ac.ict.humanmotion.abracadabra.Interface.CloudAPI
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/5/16.
 */

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var retrofit: Retrofit
    protected lateinit var cloudAPI: CloudAPI
    //    protected lateinit var rxPermissions: RxPermissions
    protected lateinit var sharedPreferences: SharedPreferences
    protected val CLOUD_BASE_URL = "http://10.41.0.133:8990/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    protected fun initSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    protected fun initRxJava() {
        retrofit = Retrofit.Builder()
                .baseUrl(CLOUD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient())
                .build()

        cloudAPI = retrofit.create(CloudAPI::class.java)
    }

    protected abstract val layoutId: Int

    protected abstract fun init()
}
