package cd.bmduka.com.View

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cd.bmduka.com.Adapter.ProduitAdapter
import cd.bmduka.com.Adapter.SliderAdapter
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.R
import cd.bmduka.com.databinding.ActivityDetaillPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetaillPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetaillPageBinding
    lateinit var id_product: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        binding = ActivityDetaillPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        id_product = bundle!!.getString("id_product").toString()
        getProduct()


        binding.back.setOnClickListener {
            onBackPressed()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fgm) as SupportMapFragment

        // Charger la carte de manière asynchrone
        mapFragment.getMapAsync { googleMap ->
            // Ajouter les marqueurs sur la carte
            val latLng1 = LatLng(-11.671828362588464, 27.480711936950684)
           // val latLng2 = LatLng(latitude2, longitude2)
            googleMap.addMarker(MarkerOptions().position(latLng1))
           // googleMap.addMarker(MarkerOptions().position(latLng2))

            // Définir les limites de la caméra pour inclure les deux points
            val bounds = LatLngBounds.Builder()
                .include(latLng1)
                //.include(latLng2)
                .build()
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))//50 est
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 20f))

            // Rendre le fragment de carte visible
            mapFragment.view?.visibility = View.VISIBLE

        }

    }
    //fonction pour recuperer les données du produit depuis la base de données

    fun getProduct(){
        binding.loaderPage.loaderFrameLayout.visibility = View.VISIBLE
        val ref = FirebaseDatabase.getInstance().getReference("Produits")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val produit = snap.getValue(Produit::class.java)
                    if (produit != null) {
                        if(produit.id_product == id_product){
                            Init(produit)
                            binding.loaderPage.loaderFrameLayout.visibility = View.GONE
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.loaderPage.loaderFrameLayout.visibility = View.GONE
                Log.d("TAG", "onCancelled: ")
            }
        })
    }
    fun Init(produit: Produit){
        binding.title.text = produit.nom
        binding.titleTop.text = produit.nom
        binding.description.text = produit.description
        binding.price.text = produit.prix.toString()+" $"

        val sliders = ArrayList<SliderModel>()
        for (image in produit.liste_images){
            sliders.add(SliderModel(image))
        }
        binding.viewpager.adapter = SliderAdapter(sliders,binding.viewpager)
        binding.viewpager.clipToPadding = false
        binding.viewpager.clipChildren = false
        binding.viewpager.offscreenPageLimit = 3
        binding.viewpager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        if (sliders.size>1){
            binding.dotsIndicator.visibility = View.VISIBLE
            binding.dotsIndicator.attachTo(binding.viewpager)
        }


    }

    //la fonction pour afficher les

}