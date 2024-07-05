package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.ItemChat
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.MessageActivity
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ItemChatBinding
import com.google.android.material.animation.AnimationUtils

class ItemChatAdapter(val liste_message:ArrayList<ItemChat>):RecyclerView.Adapter<ItemChatAdapter.ViewHolder>() {

    val viewModel = MainViewModel()
    inner class ViewHolder(val binding:ItemChatBinding):RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChatAdapter.ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemChatAdapter.ViewHolder, position: Int) {
        val item = liste_message[position]

        if (position%2==0){
            holder.itemView.animation = android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fisrt_item)
        }else{
            holder.itemView.animation = android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context, R.anim.second_iten)
        }
        viewModel.GetDataUser(item.senderId) { user->
            if (user != null){
                holder.binding.userName.text = user.name
            }else{
                holder.binding.userName.text = "Inconnu"

            }
        }
        holder.binding.lastMessage.text = item.lastMessage
        holder.itemView.setOnClickListener {
            Utils.newIntent(holder.itemView.context,MessageActivity::class.java)
        }
    }

    override fun getItemCount(): Int {
        return liste_message.size
    }


}
