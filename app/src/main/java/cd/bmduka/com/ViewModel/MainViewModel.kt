package cd.bmduka.com.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cd.babi.chatal.models.Message
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Model.User
import cd.bmduka.com.Utils.DATA
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.Utils.chatListPath
import cd.bmduka.com.Utils.messagePath
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel():ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val mydb = FirebaseDatabase.getInstance()
    val auth = Firebase.auth
    private val _messages = MutableLiveData<ArrayList<Message>>()
    val messages: LiveData<ArrayList<Message>> = _messages



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
    fun GetDataUser(mail: String=GetmailUser(),callback: (User) -> Unit) {
        val id_doc = Utils.getUID(mail)
        db.collection("users").document(id_doc).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(User::class.java)
                    if (user != null) {
                        callback(user)
                    } else{
                        callback(null!!)
                    }
                } else {
                    callback(null!!)
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
    fun logout(context: Context){
        auth.signOut()
        //supprimer les données de l'utilisateur
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

    }
    fun myUid():String{
        val uid = Utils.getUID(auth.currentUser!!.email.toString())
        return uid
    }
    fun sendMessage(message: Message){
        message.messageId = mydb.getReference(messagePath).push().key!!
        message.time = System.currentTimeMillis()
        mydb.getReference(messagePath).child(message.messageId).setValue(message)
            .addOnSuccessListener {
                mydb.getReference(chatListPath).child(message.senderId)
                    .child(message.receiverId).setValue(mapOf(
                        "id" to message.receiverId,
                        "lastMessage" to message.message,
                        "timestamp" to message.time,
                        "senderId" to message.senderId,
                        "receiverId" to message.receiverId,
                        "imageUrl" to "",
                        "conteinImage" to message.conteinImage
                    ))
                mydb.getReference(chatListPath).child(message.receiverId)
                    .child(message.senderId).setValue(mapOf(
                        "id" to message.senderId,
                        "lastMessage" to message.message,
                        "timestamp" to message.time,
                        "senderId" to message.senderId,
                        "receiverId" to message.receiverId,
                        "imageUrl" to "",
                        "conteinImage" to message.conteinImage
                    ))
            }
    }

    fun fetchMessage(receiverId: String) {
        val messageList = ArrayList<Message>()
        mydb.getReference(messagePath)
            .orderByChild("time")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        if(
                            (message.senderId ==myUid() && message.receiverId ==receiverId) || (message.senderId ==receiverId && message.receiverId==myUid())
                        ){
                            messageList.add(message)
                            _messages.value = messageList
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }





}