package kedaiapps.projeku.testandroidenamdua.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.ItemRvBottomSheetSelectionBinding
import kedaiapps.projeku.testandroidenamdua.ext.inflate

class AdapterSelection(private val onClick:(Int) -> Unit)
    : RecyclerView.Adapter<AdapterSelection.ViewHolder>() {

    var items : List<String> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_rv_bottom_sheet_selection))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var mBinding = ItemRvBottomSheetSelectionBinding.bind(containerView)

        fun bind(data: String){
            with(mBinding){
                tvLocation.text = data
                root.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
        }
    }
}