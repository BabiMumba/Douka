package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.databinding.ItemShopBinding

class ShopAdapter(val liste_boutique: ArrayList<Boutique>) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemShopBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boutique = liste_boutique[position]
        holder.binding.apply {
            nomShp.text = boutique.nom_complet
            descripBoutique.text = boutique.description
            member.text = "membre depuis ${boutique.date}"
        }
    }

    override fun getItemCount(): Int {
        return liste_boutique.size
    }
}