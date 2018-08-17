package ac.ict.humanmotion.abracadabra.Bean

class WorkListDetail {

    var type: Int = 0// 显示类型
    var isExpand: Boolean = false// 是否展开
    var childBean: WorkListDetail? = null

    var id: String? = null
    var workListName: String? = null

    var finishTime: String? = null
    var time: String? = null
    var startTime: String? = null
    var oprationPerson: String? = null

    var supervisor: String? = null

    companion object {
        val PARENT_ITEM = 0//父布局
        val CHILD_ITEM = 1//子布局
    }
}
