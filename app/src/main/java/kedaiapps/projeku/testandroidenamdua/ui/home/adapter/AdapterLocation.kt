package kedaiapps.projeku.testandroidenamdua.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.ItemLocationBinding
import kedaiapps.projeku.testandroidenamdua.ext.inflate
import kedaiapps.projeku.testandroidenamdua.helper.DataLocation
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeItem

class AdapterLocation(
    private val onClick: (DataLocation) -> Unit
) : RecyclerView.Adapter<AdapterLocation.ViewHolder>() {

    var items: MutableList<DataLocation> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_location))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var binding = ItemLocationBinding.bind(containerView)

        fun bind(data: DataLocation){
            with(binding){

                name.text = data.title

                line.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }

    fun insertData(data : List<DataLocation>){
        data.forEach {
            items.add(it)
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        if (items.isNotEmpty()) {
            items.clear()
            notifyDataSetChanged()
        }
    }
}