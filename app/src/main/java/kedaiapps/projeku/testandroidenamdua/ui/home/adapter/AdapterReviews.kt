package kedaiapps.projeku.testandroidenamdua.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kedaiapps.projeku.testandroidenamdua.R
import kedaiapps.projeku.testandroidenamdua.databinding.ItemReviewBinding
import kedaiapps.projeku.testandroidenamdua.ext.inflate
import kedaiapps.projeku.testandroidenamdua.services.entity.ResponseHomeDetailReviewsItem

class AdapterReviews : RecyclerView.Adapter<AdapterReviews.ViewHolder>() {

    var items: MutableList<ResponseHomeDetailReviewsItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_review))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var binding = ItemReviewBinding.bind(containerView)

        fun bind(data: ResponseHomeDetailReviewsItem){
            with(binding){

                Glide.with(itemView.rootView).load(data.user.image_url)
                    .apply(
                        RequestOptions()
                            .transform(RoundedCorners(16))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                    ).into(binding.image)

                binding.name.text = data.user.name
                binding.rating.text = "${data.rating}"
                binding.text.text = data.text
            }
        }
    }

    fun insertData(data : List<ResponseHomeDetailReviewsItem>){
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