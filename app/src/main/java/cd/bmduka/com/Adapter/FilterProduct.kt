package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.R
import com.bumptech.glide.Glide


class FilterProduct : RecyclerView.Adapter<FilterProduct.ViewHolder>(),Filterable{
    var items:ArrayList<Produit> = ArrayList()
        set(value) {
            field = value
            produictfilter = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produit = produictfilter[position]
        holder.bind(produit)
        holder.itemView.setOnClickListener {
            //verifier si le produit n'est regeter

        }

    }

    override fun getItemCount()=produictfilter.size

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                produictfilter = if (charSearch.isEmpty()){
                    items
                }else{
                    val resultlist = items.filter {
                        it.nom.lowercase().contains( charSearch.lowercase())
                    }
                    resultlist as ArrayList<Produit>
                }
                val filterResults = FilterResults()
                filterResults.values = produictfilter
                return filterResults
            }
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                produictfilter = p1?.values as ArrayList<Produit>
                notifyDataSetChanged()
            }

        }
    }
    private  var produictfilter:ArrayList<Produit> = ArrayList()

    inner  class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        var name: TextView = itemview.findViewById(R.id.nom_produit)
        var price:TextView = itemview.findViewById(R.id.prix_produit)
        var imageView:ImageView= itemview.findViewById(R.id.image_product)
        fun bind(produit: Produit){
            name.text = produit.nom
            price.text = produit.prix.toString()+" $"
            name.text = produit.nom
            //load image avec glide
            Glide.with(itemView.context)
                .load(produit.liste_images[0])
                .into(imageView)

        }
    }

}