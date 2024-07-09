package cd.bmduka.com.View

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.bmduka.com.Auth.Authentification
import cd.bmduka.com.Auth.LoginActivity
import cd.bmduka.com.Auth.RegisterActivity
import cd.bmduka.com.Model.User
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivityOnboardBinding

class OnboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cli_init()

    }

    fun cli_init(){
        binding.btnRegister.setOnClickListener {
            Utils.newIntent(this, RegisterActivity::class.java)
        }
        binding.btnLogin.setOnClickListener {
            Utils.newIntent(this, LoginActivity::class.java)
        }
        binding.tvInvited.setOnClickListener {
            binding.btnRegister.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.progress.visibility = View.VISIBLE
            Authentification.signInAnonymously().addOnCompleteListener {
                if(it.isSuccessful){
                    val username = "Anonyme"
                    val email = "Anonyme@gmail.com"
                    val password = "Anonyme"
                    val date = Utils.getCurrentDate()
                    val uid = it.result?.user?.uid.toString()
                    val user = User(uid,username,email,password,date)
                    binding.progress.visibility = View.GONE
                    Utils.SaveUserToFirestore(user,this)
                }else{
                    binding.progress.visibility = View.GONE
                    Utils.showToast(this, "Erreur: ${it.exception?.message}")
                    binding.btnRegister.isEnabled = true
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }
}