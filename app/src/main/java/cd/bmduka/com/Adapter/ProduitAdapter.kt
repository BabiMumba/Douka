package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.DetaillPageActivity
import cd.bmduka.com.databinding.ViewholderProductBinding
import com.bumptech.glide.Glide

class ProduitAdapter(val list_produit: ArrayList<Produit>): RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder>() {

    private var binding: ViewholderProductBinding ?= null
    class ProduitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var price:TextView
        var imageView:ImageView

        init {
            name = itemView.findViewById(R.id.nom_produit)
            price = itemView.findViewById(R.id.prix_produit)
            imageView = itemView.findViewById(R.id.image_product)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProduitAdapter.ProduitViewHolder {
        binding= ViewholderProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProduitViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ProduitAdapter.ProduitViewHolder, position: Int) {
        val item = list_produit[position]
       // val binding = holder.itemView
        holder.name.text = item.nom
        holder.price.text = item.prix.toString()+" $"
        val lien_image = item.liste_images[0]
        val context = holder.itemView.context

        Glide.with(holder.itemView.context).load(lien_image).into(holder.imageView)
        holder.itemView.setOnClickListener {
            val id_produit = item.id_product
            Utils.newIntentWithExtra(holder.itemView.context,DetaillPageActivity::class.java,"id_product",id_produit)
        }


    }

    override fun getItemCount(): Int {
        return list_produit.size
    }


}