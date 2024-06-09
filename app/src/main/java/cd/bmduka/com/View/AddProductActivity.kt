package cd.bmduka.com.View

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cd.bmduka.com.Adapter.AddProductImagesAdapter
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivityAddProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 200
    lateinit var binding: ActivityAddProductBinding
    lateinit var storageRef: StorageReference
    private var imgList = mutableListOf<Uri>()
    private val categories = arrayOf(
        "Électronique",
        "Mode et Beauté",
        "Immobilier",
        "Electroménager",
        "Pour La Maison",
        "Emplois et services",
        "Pour enfant et bébé",
        "Sports et loisirs",
        "Alimentation",
        "Vehicule(Auto et moto)",
        "Animaux",
        "Matériaux Et Equipement"
    )
    private val subCategories = mapOf(
        "Mode et Beauté" to arrayOf("Vêtements Homme", "Vêtements Femme","Chaussures Homme","Chaussures Femme","Accessoires Sacs","Accessoires Montres","Accessoires Lunettes","Accessoires Bijoux","Cosmétiques Parfums","Autres"),
        "Électronique" to arrayOf("Téléphones", "Ordinateurs", "Télévisions","Appareils photo et caméras","Tv & Vidéo","Audio & Hifi,casque","Jeux vidéo","Montres connectées","Imprimantes & Scanners","Accessoires informatiques","Accessoires téléphones","Autres"),
        "Immobilier" to arrayOf("Appartements", "Maisons", "Terrains","Villas","Immeubles","Bureaux & Commerces","Appartements meublés","Fermes et ranchs","Autres"),
        "Electroménager" to arrayOf("Cuisinières", "Réfrigérateurs", "Lave-linges","Climatiseurs","Micro-ondes","Aspirateurs","Fers à repasser","Autres"),
        "Pour La Maison" to arrayOf("Meubles", "Décoration", "Linge de maison","Vaisselles et ustensiles","Jardin et bricolage","Literie & Matelas","Autres"),
        "Emplois et services" to arrayOf("Offres d'emploi", "Demandes d'emploi", "Services","Cours particuliers","Formations professionnelles","Autres"),
        "Pour enfant et bébé" to arrayOf("Fournitures Scolaires","Vêtements bébé", "Vêtements enfant", "Chaussures bébé","Chaussures enfant","Accessoires bébé","Accessoires enfant","Chambres bébé","Chambres enfant","Jeux & Jouets","Autres"),
        "Sports et loisirs" to arrayOf("Vélos", "Art & Artisant", "Instruments de musique","Livres","Films","Jeux vidéo","Jeux & Jouets","Autres Sports & Loisirs"),
        "Vehicule(Auto et moto)" to arrayOf("Pièces détachées", "Accessoires", "Équipements","Voitures","Motos","Location de véhicules","Camions et bus","Barques et bateaux","Autres"),
        "Alimentation" to arrayOf("Fruits et légumes", "Viandes et poissons", "Boissons","Produits alimentaires","Autres produits alimentaires"),
        "Animaux" to arrayOf("Chiens", "Chats", "Oiseaux","Poissons","Matériels et accessoires pour animaux","Autres animaux"),
        "Matériaux Et Equipement" to arrayOf("Matériaux de construction", "Outillage et fournitures industrielles", "Equipements professionnels","Autres Matériaux & Equipement")

    )

    private val getImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { result ->
            imgList.addAll(result)
            if (imgList.size > 4) {
                imgList = imgList.subList(0, 4)
                Toast.makeText(this, "maximun 4 images", Toast.LENGTH_SHORT).show()
            }
            val adapter = AddProductImagesAdapter(this, imgList)
            binding.addProImagesRv.adapter = adapter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        storageRef = FirebaseStorage.getInstance().reference


        binding.btnAddProCategory.setOnClickListener {
            showCategoryDialog()
        }
        binding.addProImagesBtn.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
                getImages.launch("image/*")
            }

            //if (!checkPermission()) {
            //                requestPermission()
            //            } else {
            //                sign_in()
            //            }
        }
        binding.etatBtn.setOnClickListener {
            showStateDialog()
        }

      //  val categorie_sel = binding.tvCategorySelected.text.toString()

        binding.btnAddProPublish.setOnClickListener {
            //commencer par vérifier si les champs sont vides
            if (binding.edtName.text.toString().isEmpty()) {
                binding.edtName.error = "Veuillez entrer le nom du produit"
                return@setOnClickListener
            }
            else
            if (binding.prixArt.text.toString().isEmpty()) {
                binding.prixArt.error = "Veuillez entrer le prix du produit"
                return@setOnClickListener
            }else if (binding.tvCategorySelected.text.toString().isEmpty()){
                Toast.makeText(this, "Veuillez choisir une catégorie", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if ( imgList.isEmpty()) {
                Toast.makeText(this, "Veuillez ajouter au moins une image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
                else
            {
                binding.addProLoader.loaderFrameLayout.visibility = View.VISIBLE
                binding.btnAddProPublish.isEnabled = false

                val databaseRef = FirebaseDatabase.getInstance().getReference("Produits")
                val imagesList: MutableList<String> = mutableListOf()
                for (imageUri in imgList) {
                    val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
                    val date_auj = sdf.format(Date()).toString()
                    val imageRef = storageRef.child("Produits/${UUID.randomUUID()}")
                    imageRef.putFile(imageUri)
                        .addOnSuccessListener { taskSnapshot ->
                            // Récupérer le lien de téléchargement de l'image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                imagesList.add(uri.toString())
                                val id_product = databaseRef.push().key.toString()
                                // Vérifier si toutes les images ont été téléchargées
                                if (imagesList.size == imgList.size) {
                                    //
                                    // Toutes les images ont été téléchargées, enregistrer les informations sur Firebase Database
                                    val produit = Produit(
                                        binding.edtName.text.toString(),
                                        date_auj,
                                        binding.edtDescription.text.toString(),
                                        id_product,
                                        "id_boutique",
                                        binding.tvCategorySelected.text.toString(),
                                        binding.tvCategorySelected.text.toString(),
                                        0,
                                        binding.prixArt.text.toString().toInt(),
                                        0,
                                        imagesList

                                    )
                                    databaseRef.child(id_product).setValue(produit)
                                        .addOnSuccessListener {
                                            binding.addProLoader.loaderFrameLayout.visibility = View.GONE
                                            Utils.showToast(this, "Produit ajouté avec succès")
                                            binding.btnAddProPublish.isEnabled = true
                                        }
                                        .addOnFailureListener { e ->
                                            binding.addProLoader.loaderFrameLayout.visibility = View.GONE
                                            Toast.makeText(this, "Erreur lors de l'ajout du produit: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            binding.addProLoader.loaderFrameLayout.visibility = View.GONE
                            Toast.makeText(this, "Erreur lors du téléchargement de l'image: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            //commencer par recuperer les images importées
        }


    }
    private fun showCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choisir une catégorie")
        builder.setItems(categories) { dialog, which ->
            val selectedCategory = categories[which]
            showSubCategoryDialog(selectedCategory)
        }
        builder.show()
    }

    //fonction qui permet d'incrementer le nombre d'annce de

    private fun showSubCategoryDialog(selectedCategory: String) {
        val subCategoriesArray = subCategories[selectedCategory]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choisir une sous-catégorie")
        builder.setItems(subCategoriesArray) { dialog, which ->
            val selectedSubCategory = subCategoriesArray?.get(which)
            displaySelectedCategory(selectedCategory, selectedSubCategory)
        }
        builder.show()
    }
    private fun displaySelectedCategory(category: String, subCategory: String?) {
        val selectedCategoryText = if (subCategory != null) {
            "$category-$subCategory"
        } else {
            category
        }
        binding.tvCategorySelected.text = selectedCategoryText
    }

    private fun checkPermission(): Boolean {
        //log version
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
                }
            }
        }
    }

    private fun showStateDialog() {
        val states = arrayOf("Neuf","D'occasion(comme neuf)","D'occasion(bon état)","D'occasion(assez bon état)","Rétouche(mais bon état)")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sélectionner l'état du produit")
            .setSingleChoiceItems(states, -1) { dialog, which ->
                val selectedState = states[which]
                binding.tvEtat.text = selectedState
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

}