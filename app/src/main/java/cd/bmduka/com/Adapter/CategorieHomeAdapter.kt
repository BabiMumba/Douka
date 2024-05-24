package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.homecategorie
import cd.bmduka.com.R
import com.bumptech.glide.Glide

class CategorieHomeAdapter(
    val items: ArrayList<homecategorie>,
):RecyclerView.Adapter<CategorieHomeAdapter.CategorieViewHolder>() {
    inner class CategorieViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
     //  val image: ImageView = itemView.findViewById(R.id.categorie_image)
        val title: TextView = itemView.findViewById(R.id.categories_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.dot_categories,parent,false)
        return CategorieViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
       /* Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.image)*/
        //les elements de la liste apartir de 1 la marge est de 10dp
        if (position == 0){
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0,0,0,0)
            holder.itemView.layoutParams = params
        }
    }

    override fun getItemCount() = items.size
}