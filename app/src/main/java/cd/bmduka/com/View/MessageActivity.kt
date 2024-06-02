package cd.bmduka.com.View

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cd.babi.chatal.models.Message
import cd.bmduka.com.Adapter.MessageAdapter
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ActivityMessageBinding


class MessageActivity : AppCompatActivity() {
   var liste_message= ArrayList<Message>()
    lateinit var binding:ActivityMessageBinding
    private var viewModel= MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitMessages()

        binding.btnSend.setOnClickListener {
            val message = binding.message.text.toString()
            if (message.isNotEmpty()){
                viewModel.addMessage(message)
                binding.message.setText("")
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.message.windowToken,0)
            //    binding.recycleMessage.scrollToPosition(liste_message.size-1)
            }else{
                Utils.showToast(this,"votre message")
            }
        }



    }


    fun InitMessages(){
        viewModel.messages.observe(this){items->
            for (item in items){
                liste_message.add(item)
            }
            binding.recycleMessage.apply {
                adapter = MessageAdapter(liste_message)
            }
        }
        viewModel.FetchMessage()
    }
}