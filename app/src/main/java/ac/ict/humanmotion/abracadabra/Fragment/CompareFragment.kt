package ac.ict.humanmotion.abracadabra.Fragment

import ac.ict.humanmotion.abracadabra.R

class CompareFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_compare

    override fun init() {
        findViewById(R.id.compare_text).setOnClickListener {
            
        }
    }
}