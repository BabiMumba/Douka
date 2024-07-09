package cd.bmduka.com.Auth

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import cd.bmduka.com.MainActivity
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var mainViewModel: MainViewModel
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {
            Utils.isloading(binding.btnLogin,binding.progress,true)
            val email = binding.edtEmail.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                mainViewModel.GetUsername {name->
                                    Utils.savename(this,name,true)
                                }
                                Utils.isloading(binding.btnLogin,binding.progress,false)
                                Utils.newIntent(this,MainActivity::class.java)
                            }else{
                                Utils.isloading(binding.btnLogin,binding.progress,false)
                                Utils.showToast(this,it.exception.toString())
                            }
                        }

            }else{
                Utils.isloading(binding.btnLogin,binding.progress,false)
                Utils.showToast(this,"Please Enter Email and Password")
            }
        }

    }
}