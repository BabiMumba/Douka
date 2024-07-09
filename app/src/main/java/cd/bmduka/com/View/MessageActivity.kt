package cd.bmduka.com.View

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cd.babi.chatal.models.Message
import cd.bmduka.com.Adapter.MessageAdapter
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ActivityMessageBinding
import cd.bmduka.com.databinding.DialogMsgChatBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class MessageActivity : AppCompatActivity() {
    var liste_message = ArrayList<Message>()
    private val PERMISSION_REQUEST_CODE = 200
    lateinit var binding: ActivityMessageBinding
    private val viewModel = MainViewModel()
    lateinit var myadapter: MessageAdapter
    var status_image = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myadapter = MessageAdapter(liste_message)
        binding.recycleMessage.adapter = myadapter

        val id_receiver = intent.getStringExtra("id_sender").toString()
        val mesg_txt = intent.getStringExtra("msg").toString()

        InitMessages(id_receiver)
        binding.message.setText(mesg_txt)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.itemCameraIcon.setOnClickListener {
            if (checkPermission()) {
                getImages.launch("image/*")
            } else {
                requestPermission()
            }
        }
        binding.btnSend.setOnClickListener {
            val message = binding.message.text.toString()
            if (message.isNotEmpty()) {
                val messages = Message(
                    viewModel.myUid(),
                    id_receiver,
                    message,
                    conteinImage = status_image,
                    imageUrl = ""
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

    private fun InitMessages(id_receiver: String) {
        viewModel.fetchMessage( id_receiver)
        viewModel.messages.observe(this) { messageList ->
            liste_message.clear()
            liste_message.addAll(messageList)
            myadapter.notifyDataSetChanged()
            binding.recycleMessage.scrollToPosition(liste_message.size - 1)
        }

        viewModel.GetDataUser(id_receiver) {user->
            if (user != null){
                binding.userName.text = user.name
            }else{
                binding.userName.text = "Inconnu"
            }

        }
    }

    private fun checkPermission(): Boolean {
        //log version de l'appareil
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                ), PERMISSION_REQUEST_CODE
            )
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {PERMISSION_REQUEST_CODE -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée
                getImages.launch("image/*")

            } else {
                //VOID.to(this,"Vous devez accepter la permission de lecture et d'écriture pour continuer")
                Toast.makeText(
                    this,
                    "Vous devez valider les permissions pour continuer",
                    Toast.LENGTH_SHORT
                ).show()
                //redeamnder la permission
                requestPermission()
            }
        }
        }
    }
    //fonction getImages pour recuperer une image dans la gallerie
    private val getImages = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val dialogue = Dialog(this)
            dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = DialogMsgChatBinding.inflate(layoutInflater)
            dialogue.setContentView(view.root)
            val lp = dialogue.window!!.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT


            val image = view.imageSend
            val send = view.btnSend
            val myprogressbar = view.progress

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            image.setImageBitmap(bitmap)
            send.setOnClickListener {
                myprogressbar.visibility = View.VISIBLE
                val imageRef = FirebaseStorage.getInstance().reference.child("imageschat/${UUID.randomUUID()}")
                val uploadTask = imageRef.putFile(uri)
                uploadTask.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val msg = view.message.text.toString()
                        val messages = Message(
                            viewModel.myUid(),
                            intent.getStringExtra("id_sender").toString(),
                            msg,
                            conteinImage = true,
                            imageUrl = uri.toString()
                        )
                        viewModel.sendMessage(messages)
                        dialogue.dismiss()
                    }
                }.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    myprogressbar.progress = progress.toInt()

                }.addOnFailureListener {
                    Log.e("Error", it.message.toString())
                    dialogue.dismiss()
                }
            }
            dialogue.show()
            dialogue.window!!.attributes = lp

        }
    }


}