package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.DetailleShopActivity
import cd.bmduka.com.databinding.ItemShopBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ShopAdapter(val liste_boutique: ArrayList<Boutique>) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemShopBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boutique = liste_boutique[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_shop)
        holder.binding.apply {
            nomShp.text = boutique.nom_complet
            descripBoutique.text = boutique.description
            member.text = "membre depuis ${boutique.date}"
        }
        holder.itemView.setOnClickListener {
            handleBoutiqueClick(boutique, holder)
        }
    }

    private fun handleBoutiqueClick(boutique: Boutique, holder: ViewHolder) {
        val theme = boutique.getThemeColorOrDefault()
        if (theme.isNotEmpty()) {
            Utils.newIntentWithExtra(
                holder.itemView.context,
                DetailleShopActivity::class.java,
                "theme",
                theme
            )
        } else {
            Utils.newIntentWithExtra(
                holder.itemView.context,
                DetailleShopActivity::class.java,
                "theme",
                "#ff4747"
            )
        }
    }

    private fun Boutique.getThemeColorOrDefault(defaultValue: String = "") =
        theme_color ?: defaultValue

    override fun getItemCount(): Int {
        return liste_boutique.size
    }
}