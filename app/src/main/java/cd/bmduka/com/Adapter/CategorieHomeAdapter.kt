package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Fragment.SearchFragment
import cd.bmduka.com.Model.homecategorie
import cd.bmduka.com.R
import com.bumptech.glide.Glide

class CategorieHomeAdapter(
    val items: ArrayList<homecategorie>,
):RecyclerView.Adapter<CategorieHomeAdapter.CategorieViewHolder>() {
    inner class CategorieViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
         val image: ImageView = itemView.findViewById(R.id.img_dot_categoris)
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
        if (true){
            Glide.with(holder.itemView.context)
                .load(item.image)
                .into(holder.image)
        }
        //les elements de la liste apartir de 1 la marge est de 10dp
        if (position == 0){
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0,0,0,0)
            holder.itemView.layoutParams = params
        }
        holder.itemView.setOnClickListener {
            val categorie = items[position].title
            val fragment = SearchFragment()
            val bundle = android.os.Bundle()
            bundle.putString("categorie",categorie)
            fragment.arguments = bundle
            val transaction = (holder.itemView.context as androidx.fragment.app.FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount() = items.size
}