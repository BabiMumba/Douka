package cd.bmduka.com.Model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.R
import com.google.android.material.card.MaterialCardView

class FiltreAdapter(val filtres: ArrayList<Filtre>): RecyclerView.Adapter<FiltreAdapter.FiltreViewHolder>() {

    //la methode clickListener est une fonction qui prend un filtre en parametre et ne retourne rien
    private var onItemClickListener:OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(item: Filtre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FiltreViewHolder(view)
    }

    override fun onBindViewHolder(holder: FiltreViewHolder, position: Int) {
        holder.bind(filtres[position],position)

    }

    override fun getItemCount(): Int {
        return filtres.size
    }

    inner class FiltreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("UseCompatLoadingForColorStateLists", "NotifyDataSetChanged")
        fun bind(filtre: Filtre,position: Int) {
            itemView.findViewById<TextView>(R.id.nom_filter).text = filtre.nom
            itemView.setOnClickListener {
                val clickedFilter = filtres[position]
                clickedFilter.isSelected = true

                // Désélectionner les autres filtres
                for (i in filtres.indices) {
                    if (i != position) {
                        filtres[i].isSelected = false
                    }
                }

                onItemClickListener?.onItemClick(clickedFilter)
                notifyDataSetChanged()
            }

            // Changer la couleur du filtre sélectionné
            //val color = ContextCompat.getColorStateList(itemView.context, R.color.Accent)
            itemView.findViewById<MaterialCardView>(R.id.card_filter).backgroundTintList =
                if (filtre.isSelected) {
                    val color = ContextCompat.getColorStateList(itemView.context, R.color.Accent)
                    color
                } else {
                    val color = ContextCompat.getColorStateList(itemView.context, R.color.white)
                    color
                }
        }
    }
}