package cd.bmduka.com.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Adapter.ItemChatAdapter
import cd.bmduka.com.Model.ItemChat
import cd.bmduka.com.R
import cd.bmduka.com.databinding.FragmentChatBinding


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        iniTMessage()
        return binding.root
    }

    fun iniTMessage(){
        val liste_message = ArrayList<ItemChat>()
        liste_message.add(ItemChat("babi","bonjour vous allez bien"))
        liste_message.add(ItemChat("Malothy","Comment puis-je vous contacter"))
        liste_message.add(ItemChat("Randy","Je cherche la marque luis vuiton"))
        liste_message.add(ItemChat("Elkana","toujours a la meme adresse"))
        liste_message.add(ItemChat("Marie","Les stock est dispo"))
        liste_message.add(ItemChat("Levis","Nous sommes vraiment desoles"))
        binding.recyclerChat.apply {
            adapter=ItemChatAdapter(liste_message)
        }
        binding.recyclerChat.adapter!!.notifyDataSetChanged()
    }

}