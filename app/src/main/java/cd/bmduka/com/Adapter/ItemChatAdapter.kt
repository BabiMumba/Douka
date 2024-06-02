package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Model.ItemChat
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.MessageActivity
import cd.bmduka.com.databinding.ItemChatBinding

class ItemChatAdapter(val liste_message:ArrayList<ItemChat>):RecyclerView.Adapter<ItemChatAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:ItemChatBinding):RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChatAdapter.ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemChatAdapter.ViewHolder, position: Int) {
        val item = liste_message[position]
        holder.binding.userName.text = item.name
        holder.binding.lastMessage.text = item.lastMessage
        holder.itemView.setOnClickListener {
            Utils.newIntent(holder.itemView.context,MessageActivity::class.java)
        }
    }

    override fun getItemCount(): Int {
        return liste_message.size
    }


}
