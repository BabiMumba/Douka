package cd.bmduka.com.View

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditProfileBinding
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditProfileBinding.inflate(layoutInflater)

        mainViewModel = MainViewModel()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val actualname = Utils.username(this)
        binding.edtName.setText(actualname)
        binding.btnSave.setOnClickListener {
            Utils.isloading(binding.btnSave,binding.progress,true)
            val name = binding.edtName.text.toString()
            mainViewModel.UpdateUsername(name){
                if(it){
                    Utils.showToast(this, "Mise à jour effectuée")
                    Utils.isloading(binding.btnSave,binding.progress,false)
                    Utils.savename(this,name)
                    onBackPressed()
                }else {
                    Utils.isloading(binding.btnSave,binding.progress,false)
                    Utils.showToast(this, "Erreur lors de la mise à jour")
                    Log.d("EditProfileActivity", "Erreur lors de la mise à jour")

                }

            }

        }
        setContentView(binding.root)

    }
}