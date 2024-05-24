package cd.bmduka.com.Auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.bmduka.com.MainActivity
import cd.bmduka.com.Model.User
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegister.setOnClickListener {
            val username = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.password.text.toString()
            val date = Utils.getCurrentDate()
            val uid = Utils.GetUserId()
            if(checkFields()){
                Utils.isloading(binding.btnRegister,binding.progress,true)
               val createUser =Authentification.createUserWithEmail(email, password,  username)
                createUser.addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = User(uid,username,email,password,date)
                        SaveUserToFirestore(user)
                    }else{
                        Utils.isloading(binding.btnRegister,binding.progress,false)
                        Utils.showToast(this, "Erreur: ${it.exception?.message}")
                    }
                }



            }
        }

        binding.btnGoogle.setOnClickListener {
           // val gso
        }

    }
    //verifier si tous les champs sont remplis
    private fun checkFields(): Boolean {
        if (binding.edtName.text.toString().isEmpty()) {
            Utils.showToast(this, "Veuillez entrer votre nom")
            return false
        }
        if (binding.edtEmail.text.toString().isEmpty()) {
            Utils.showToast(this, "Veuillez entrer votre email")
            return false
        }
        //verifier si l'email est valide
        if (!Utils.isValidEmail(binding.edtEmail.text.toString())) {
            Utils.showToast(this, "Veuillez entrer un email valide")
            return false
        }
        if (binding.password.text.toString().isEmpty()) {
            Utils.showToast(this, "Veuillez entrer votre mot de passe")
            return false
        }
        if (binding.password.text.toString().length < 6) {
            Utils.showToast(this, "Le mot de passe doit contenir au moins 6 caractères")
            return false
        }
        //verifier le check box de terme et condition
        if (!binding.accepteTerme.isChecked) {
            Utils.showToast(this, "Veuillez accepter les termes et conditions")
            return false
        }
        return true
    }

    fun SaveUserToFirestore(user: User){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Utils.isloading(binding.btnRegister,binding.progress,false)
                    Utils.newIntentFinish(this, MainActivity::class.java)
                }else{
                    Utils.isloading(binding.btnRegister,binding.progress,false)
                    Utils.showToast(this, "Erreur: ${it.exception?.message}")
                }
            }
    }
}