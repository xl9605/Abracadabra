package ac.ict.humanmotion.abracadabra.Adapter

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/12.
 */

class MyViewPager : ViewPager {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = false
}
