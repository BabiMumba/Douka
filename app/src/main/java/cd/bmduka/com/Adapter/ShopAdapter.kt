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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
        val email = FirebaseAuth.getInstance().currentUser?.email
        val id= Utils.getUID(email.toString())
        holder.itemView.setOnClickListener {
            handleBoutiqueClick(boutique, holder)
        }
        holder.binding.btnabonner.setOnClickListener {
            //
            FirebaseDatabase.getInstance().getReference("Abonner").child(boutique.id_boutique.toString()).child(id).setValue(true)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(holder.itemView.context, "Vous êtes abonné à cette boutique", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(holder.itemView.context, "Erreur lors de l'abonnement", Toast.LENGTH_SHORT).show()
                    }
                }

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