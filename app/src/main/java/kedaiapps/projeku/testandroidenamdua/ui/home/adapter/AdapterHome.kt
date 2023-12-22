package kedaiapps.projeku.testandroidenamdua.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.ItemHomeBinding
import kedaiapps.projeku.testandroidenamdua.ext.inflate
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeItem

class AdapterHome (
    private val onClick: (ResponseHomeItem) -> Unit
) : RecyclerView.Adapter<AdapterHome.ViewHolder>() {

    var items: MutableList<ResponseHomeItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_home))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var binding = ItemHomeBinding.bind(containerView)

        fun bind(data: ResponseHomeItem){
            with(binding){

                Glide.with(itemView.rootView).load(data.image_url)
                    .apply(
                        RequestOptions()
                            .transform(RoundedCorners(16))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                    ).into(binding.image)

                val jarak = data.distance.split(".")
                val hasil = jarak[0].toDouble()/1000

                binding.judul.text = data.name
                binding.rating.text = "${data.rating} (${data.review_count})"
                binding.phone.text = data.display_phone
                binding.distance.text = "$hasil km"

                line.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }

    fun insertData(data : List<ResponseHomeItem>){
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