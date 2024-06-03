package cd.bmduka.com.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cd.babi.chatal.models.Message
import cd.bmduka.com.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel():ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner
    val liste_message= MutableLiveData<ArrayList<Message>>()
    val messages: MutableLiveData<ArrayList<Message>> = liste_message

    val liste = ArrayList<Message>()


    fun loadBanners(){
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listes = mutableListOf<SliderModel>()
                for (snap in snapshot.children){
                    val banner = snap.getValue(SliderModel::class.java)
                    if (banner != null){
                        listes.add(banner)
                    }
                }
                _banner.value = listes


            }

            override fun onCancelled(error: DatabaseError) {
                _banner.value = null
            }
        })
    }

    fun FetchMessage(){
        liste.add(Message("bonjour",false))
        liste.add(Message("comment tu va",true))
        liste.add(Message("bien et toi",false))
        liste_message.value = liste
    }
    fun addMessage(msg:String){
        liste.add(Message(msg,true))
    }

}