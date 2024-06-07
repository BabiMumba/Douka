package cd.bmduka.com.View

import android.os.Bundle
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

        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString()
            mainViewModel.UpdateUsername(name){
                if(it){
                    onBackPressed()
                }else {
                    Utils.showToast(this, "Erreur lors de la mise à jour")
                }

            }

        }
        setContentView(binding.root)

    }
}