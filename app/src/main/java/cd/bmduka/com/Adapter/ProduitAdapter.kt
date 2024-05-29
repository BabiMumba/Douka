package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.R
import cd.bmduka.com.databinding.ViewholderProductBinding

class ProduitAdapter(val list_produit: ArrayList<Produit>): RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder>() {

    private var binding: ViewholderProductBinding ?= null
    class ProduitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var price:TextView

        init {
            name = itemView.findViewById(R.id.nom_produit)
            price = itemView.findViewById(R.id.prix_produit)
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
        holder.price.text = item.prix.toString()


    }

    override fun getItemCount(): Int {
        return list_produit.size
    }


}