package ac.ict.humanmotion.abracadabra.Lpms

import android.support.v4.app.Fragment

abstract class MyFragment : Fragment() {

    abstract var myFragmentTag: Int

    interface MyFragmentListener {
        fun onUserInput(mode: Int, data: String)
    }

    abstract fun updateView(d: LpmsBData, s: ImuStatus)

//    abstract fun getMyFragmentTag():Int
}
