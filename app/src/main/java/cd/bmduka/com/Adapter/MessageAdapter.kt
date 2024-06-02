package cd.bmduka.com.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cd.babi.chatal.models.Message
import cd.bmduka.com.databinding.ReceivedMessageItemBinding
import cd.bmduka.com.databinding.SentMessageItemBinding

class MessageAdapter(val liste_message:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType){
            0->{
                ViewHolderReceived(
                    ReceivedMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else->{
                ViewHolderSent(
                    SentMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return liste_message.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = liste_message[position]
        when (holder) {
            is ViewHolderReceived -> {
                holder.bind(currentItem)
            }
            is ViewHolderSent -> {
                holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (liste_message[position].isReceived){
            1
        }else 0
    }
    inner class ViewHolderReceived(private val binding: ReceivedMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.tvMessage.text = item.message
        }
    }
    inner class ViewHolderSent(private val binding: SentMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.tvMessage.text = item.message
            /*
            if(position==dataList.size-1){
                if(item.seen){
                    binding.seen.visibility = View.VISIBLE
                }
                else{
                    binding.seen.text = itemView.context.getString(R.string.sent)
                    binding.seen.visibility = View.VISIBLE
                }
            }
            */
        }
    }


}