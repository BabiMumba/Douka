package cd.bmduka.com.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Adapter.ItemChatAdapter
import cd.bmduka.com.Model.ItemChat
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.Utils.chatListPath
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private val viewModel = MainViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        if (Utils.IsConnected(requireContext())){
            iniTMessage()
        }else{
            Utils.showToast(requireContext(),"Veuillez vous connecter")
        }
        return binding.root
    }

    fun iniTMessage(){
        val liste_message = ArrayList<ItemChat>()
        FirebaseDatabase.getInstance().getReference(chatListPath).child(viewModel.myUid()).orderByChild("timestamp")
            .addChildEventListener(object : ValueEventListener, com.google.firebase.database.ChildEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val itemChat = snap.getValue(ItemChat::class.java)
                        if (itemChat != null){
                            liste_message.add(itemChat)
                            Log.d("message",itemChat.toString())
                        }
                    }
                   val adapter = ItemChatAdapter(liste_message)
                    binding.recyclerChat.adapter = adapter
                }
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val itemChat = snapshot.getValue(ItemChat::class.java)
                    if (itemChat != null){
                        liste_message.add(itemChat)
                        Log.d("message",itemChat.toString())
                    }
                    val adapter = ItemChatAdapter(liste_message)
                    binding.recyclerChat.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("error",error.message)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            })
    }


    override fun onResume() {
        iniTMessage()
        super.onResume()
    }

}