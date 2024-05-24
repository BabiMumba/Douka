package cd.bmduka.com

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cd.bmduka.com.Fragment.HomeFragment
import cd.bmduka.com.Fragment.ProfileFragment
import cd.bmduka.com.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled=false

        binding.bottomNavigationView.selectedItemId = R.id.homeMenu
        loadFragment(HomeFragment())
        inifragment()







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

            }
            false
        }

    }

    fun inibanner(){
        binding.bottomNavigationView
    }
}