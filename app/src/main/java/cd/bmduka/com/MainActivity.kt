package cd.bmduka.com

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cd.bmduka.com.Fragment.ChatFragment
import cd.bmduka.com.Fragment.FavoriFragment
import cd.bmduka.com.Fragment.HomeFragment
import cd.bmduka.com.Fragment.ProfileFragment
import cd.bmduka.com.Fragment.ShopFragment
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddProductActivity
import cd.bmduka.com.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMainBinding
    private lateinit var locationUtils: Utils.LocationUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2)

        binding.bottomNavigationView.selectedItemId = R.id.homeMenu
        loadFragment(HomeFragment())
        inifragment()


        binding.addBtn.setOnClickListener {
            if (checkIseller()){
                Utils.newIntent(this, AddProductActivity::class.java)
            }else{
                Utils.showToast(this, "Créez une boutique pour ajouter un produit")
            }
        }

        locationUtils = Utils.LocationUtils(this)
        locationUtils.getCurrentLocation { latitude, longitude ->
            // Utilisez les coordonnées ici, par exemple pour les afficher sur la carte
            Log.d("MyMapsActivity", "Latitude: $latitude, Longitude: $longitude")
        }

    }
    fun loadFragment(fragment: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun inifragment(){
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMenu -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.profileMenu -> {
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.chat_fragment -> {
                    loadFragment(ChatFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.shop -> {
                    loadFragment(ShopFragment())
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }
    fun checkIseller():Boolean{
        val isSeller = Utils.IsVendeur(this)
        return isSeller
    }

    override fun onResume() {
        super.onResume()
        checkIseller()
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }


}