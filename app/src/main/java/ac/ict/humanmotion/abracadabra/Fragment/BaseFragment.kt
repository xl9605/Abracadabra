package ac.ict.humanmotion.abracadabra.Fragment

import ac.ict.humanmotion.abracadabra.Interface.CloudAPI
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/5/15.
 */

abstract class BaseFragment : Fragment() {
    protected val CLOUD_BASE_URL = "http://10.41.0.133:8990/v1/"
    protected lateinit var mActivity: Activity
    protected lateinit var contentView: View

    protected var cloudAPI: CloudAPI = Retrofit.Builder()
            .baseUrl(CLOUD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CloudAPI::class.java)

    /**
     * 说明：在此处保存全局的Context

     * @param context 上下文
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(layoutId, container, false)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    protected abstract val layoutId: Int

    protected abstract fun init()

    fun findViewById(id: Int): View = contentView.findViewById<View>(id)
}
