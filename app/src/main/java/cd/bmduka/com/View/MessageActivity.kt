package cd.bmduka.com.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cd.babi.chatal.models.Message
import cd.bmduka.com.Adapter.MessageAdapter
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ActivityMessageBinding


class MessageActivity : AppCompatActivity() {
    var liste_message = ArrayList<Message>()
    lateinit var binding: ActivityMessageBinding
    private val viewModel = MainViewModel()
    lateinit var myadapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myadapter = MessageAdapter(liste_message)
        binding.recycleMessage.adapter = myadapter

        val id_sender = "babi"
        InitMessages(id_sender)

        binding.btnSend.setOnClickListener {
            val message = binding.message.text.toString()
            if (message.isNotEmpty()) {
                val messages = Message(
                    viewModel.myUid(),
                    id_sender,
                    message
                )
                viewModel.sendMessage(messages)
                binding.message.setText("")
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.message.windowToken, 0)
            } else {
                Utils.showToast(this, "Votre message ne peut pas être vide.")
            }
        }
    }

    private fun InitMessages(id_sender: String) {
        viewModel.fetchMessage(viewModel.myUid(), id_sender)
        viewModel.messages.observe(this) { messageList ->
            liste_message.clear()
            liste_message.addAll(messageList)
            myadapter.notifyDataSetChanged()
            binding.recycleMessage.scrollToPosition(liste_message.size - 1)
        }
    }
}