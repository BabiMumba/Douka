package cd.bmduka.com.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.bmduka.com.Adapter.ShopAdapter.OnItemClickListener
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.DetailleShopActivity
import cd.bmduka.com.databinding.ItemShoHorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.NonDisposableHandle.parent

class ShopCartAdapter(val liste_boutique: ArrayList<Boutique>) :
    RecyclerView.Adapter<ShopCartAdapter.ViewHolder>() {
        //onclick method

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemShoHorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShoHorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boutique = liste_boutique[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_shop)
        holder.binding.apply {
            shopName.text = boutique.nom_complet
            category.text = boutique.description
        }
        val mylatitud = Utils.getLatitude()
        val mylongitud = Utils.getLongitude()

        Log.d("TAG", "onBindViewHolder: $mylatitud")
        Log.d("TAG", "onBindViewHolder: $mylongitud")
        val distance = Utils.calculateDistance(
            mylatitud!!,
            mylongitud!!,
            boutique.coordonne.latitude,
            boutique.coordonne.longitude
        )
        //coordonne btque
        Log.d("TAG", "Latitude boutique $position: ${boutique.coordonne.latitude}")
        Log.d("TAG", "Longitude boutique $position: ${boutique.coordonne.longitude}")
        Log.d("TAG", "Distance boutique $position: $distance")
        //afficher en km si la distance est superieur a 1km est en metre si la distance est inferieur a 1km
        if (distance > 1) {
            //prendre 2 chiffres apres la virgule
            holder.binding.distance.text = String.format("%.2f", distance) + " km"
        } else {
            holder.binding.distance.text = (distance * 1000).toInt().toString() + " m"
        }

        holder.binding.btnabonner.setOnClickListener {
            handleBoutiqueClick(boutique, holder)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(boutique)
        }
    }

    private fun handleBoutiqueClick(boutique: Boutique, holder: ViewHolder) {
        val id_boutique = boutique.id_boutique.toString()
        val theme = boutique.getThemeColorOrDefault()
        if (theme.isNotEmpty()) {
            Utils.newIntentWithExtra(
                holder.itemView.context,
                DetailleShopActivity::class.java,
                "theme",
                theme,
                "id_btq",
                id_boutique
            )
        } else {
            Utils.newIntentWithExtra(
                holder.itemView.context,
                DetailleShopActivity::class.java,
                "theme",
                "#ff4747",
                "id_btq",
                id_boutique
            )
        }
    }

    private fun Boutique.getThemeColorOrDefault(defaultValue: String = "") =
        theme_color ?: defaultValue

    override fun getItemCount(): Int {
        return liste_boutique.size
    }
}