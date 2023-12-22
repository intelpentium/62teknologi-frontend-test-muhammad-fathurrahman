package kedaiapps.projeku.testandroidenamdua.utils

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.BottomSheetSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSelection<T> : BottomSheetDialogFragment() {

    lateinit var mBinding: BottomSheetSelectionBinding
    interface Listener<H>{
        fun onSelected(data :H)
    }

    var dataList : ArrayList<T> = ArrayList()
    var dataListString : ArrayList<String> = ArrayList()
    var title = ""
    private val adapter : AdapterSelection by lazy(LazyThreadSafetyMode.NONE){
        AdapterSelection(::onClick)
    }
    var listener : Listener<T>?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BottomSheetSelectionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)

        mBinding.tvTitle.text = title
        mBinding.rv.adapter = adapter
        adapter.items = dataListString
    }
    private fun onClick(position : Int){
        listener?.onSelected(dataList[position])
        dismiss()
    }
    class Builder<T> {
        private var tag: String? = null

        fun build(listener : Listener<T>, dataList : ArrayList<T>, dataListString : ArrayList<String>, title:String): BottomSheetSelection<T> {
            val args = Bundle()

            val fragment = BottomSheetSelection<T>()
            fragment.dataList = dataList
            fragment.title = title
            fragment.dataListString = dataListString
            fragment.arguments = args
            fragment.listener = listener
            return fragment
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}