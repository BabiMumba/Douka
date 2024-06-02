package cd.bmduka.com.View

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.babi.chatal.models.Message
import cd.bmduka.com.Adapter.MessageAdapter
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    var liste_message= ArrayList<Message>()
    lateinit var binding:ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitMessage()
        binding.btnSend.setOnClickListener {
            val message = binding.message.text.toString()
            if (message.isNotEmpty()){
                liste_message.add(Message(message,true))
                binding.message.setText("")
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.message.windowToken,0)
            }else{
                Utils.showToast(this,"votre message")
            }
        }
        binding.recycleMessage.adapter!!.notifyDataSetChanged()


    }

    fun InitMessage(){
        liste_message.add(Message("bonjour",false))
        liste_message.add(Message("comment tu va",true))
        liste_message.add(Message("bien et toi",false))
        binding.recycleMessage.apply {
            adapter = MessageAdapter(liste_message)

        }
        binding.recycleMessage.adapter!!.notifyDataSetChanged()
    }
}