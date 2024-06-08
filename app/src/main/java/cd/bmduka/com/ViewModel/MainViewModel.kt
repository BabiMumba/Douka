package cd.bmduka.com.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cd.babi.chatal.models.Message
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.Model.User
import cd.bmduka.com.Utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel():ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val db = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
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

    fun GetmailUser():String{
        val mail = auth.currentUser!!.email.toString()
        return mail
    }

    //fonction asynchrone pour recuperer le nom de l'utilisateur
    fun GetUsername(callback: (String) -> Unit) {
        val mail = GetmailUser()
        val id_doc = Utils.getUID(mail)
        db.collection("users").document(id_doc).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(User::class.java)
                    if (user != null) {
                        callback(user.name)
                    } else {
                        callback("Marcelo")
                    }
                } else {
                    callback("Marcelo")
                }
            }
    }
    //fonction asynchrone pour update le nom de l'utilisateur
    fun UpdateUsername(name: String, callback: (Boolean) -> Unit) {
        val mail = GetmailUser()
        val id_doc = Utils.getUID(mail)
        db.collection("users").document(id_doc).update("name", name)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true)
                } else {
                    Log.d("TAG", "update failed"+it.exception!!.message)
                    callback(false)
                }
            }
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

    fun CreateUser(mail:String,password:String,name:String){
        auth.createUserWithEmailAndPassword(mail,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val uid = it.result.user!!.uid
                    val date = Utils.getCurrentDate()
                    val user = User(uid,name,mail,password,date)
                    SaveUser(user)
                }
            }
    }

    fun SaveUser(user: User){
        db.collection("users").document(user.uid).set(user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("TAG","user saved")
                }else{
                    Log.d("TAG","user not saved"+it.exception!!.message)
                }
            }
    }

    fun logout(){
        auth.signOut()

    }



}