package cd.bmduka.com.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions

class SliderAdapter(
    private var sliderItems: List<SliderModel>,
    private val viewpager: ViewPager2

): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private lateinit var context: Context
    private val runnable = Runnable {
        sliderItems = sliderItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
            .inflate(R.layout.slider_container,parent,false)
        return SliderViewHolder(inflater)
    }
    inner class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.slider_image)
        fun setImage(sliderItems: SliderModel,context: Context) {
            val requestOptions = RequestOptions().transform(CenterInside())
            Glide.with(context)
                .load(sliderItems.url)
                .apply(requestOptions)
                .into(image)
        }

    }


    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position],context)
        if (position == sliderItems.size - 1){
            viewpager.post(runnable)
        }
        //si l'utilisateur clique sur l'image du slider
        holder.image.setOnClickListener {

        }

    }

    override fun getItemCount() = sliderItems.size
}