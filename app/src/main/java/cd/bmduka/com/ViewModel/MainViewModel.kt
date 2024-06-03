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
import perfetto.protos.UiState

class MainViewModel():ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner
    private val _messages = MutableLiveData<ArrayList<Message>>()
    val messages: LiveData<ArrayList<Message>> = _messages

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState


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


    fun fetchMessages() {
        _uiState.value = UiState.Loading
        val liste = arrayListOf(
            Message("bonjour", false),
            Message("comment tu va", true),
            Message("bien et toi", false)
        )
        _messages.value = liste
        _uiState.value = UiState.Success
    }

    fun addMessage(msg: String) {
        val updatedList = _messages.value ?: arrayListOf()
        updatedList.add(Message(msg, true))
        _messages.value = updatedList
    }
    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }



}