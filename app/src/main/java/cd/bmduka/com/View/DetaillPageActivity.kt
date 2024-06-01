package cd.bmduka.com.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cd.bmduka.com.R
import cd.bmduka.com.databinding.ActivityDetaillPageBinding

class DetaillPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetaillPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        binding = ActivityDetaillPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }
}