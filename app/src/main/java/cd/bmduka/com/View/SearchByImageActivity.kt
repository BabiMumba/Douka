package cd.bmduka.com.View

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivitySearchByImageBinding


class SearchByImageActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchByImageBinding
  //  private lateinit var imageLabeler: ImageLabeler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchByImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f) // Seuil de confiance pour les étiquettes
            .build()*/
       // imageLabeler= ImageLabeling.getClient(options)
        binding.takePhotoBtn.setOnClickListener {
            takePhoto()
        }
    }
    private fun takePhoto() {
        // Ouvrir l'appareil photo pour prendre une photo
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            binding.progressBar.visibility= View.VISIBLE
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageCap.setImageBitmap(imageBitmap)
           // analyzeImage(imageBitmap)
        }
    }



    companion object {
        private const val REQUEST_CODE_TAKE_PHOTO = 123
    }
}