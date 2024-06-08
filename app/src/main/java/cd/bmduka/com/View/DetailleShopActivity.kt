package cd.bmduka.com.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import cd.bmduka.com.Adapter.ViewPagerAdapter
import cd.bmduka.com.Fragment.tabs.TabsHomeFragment
import cd.bmduka.com.R
import cd.bmduka.com.databinding.ActivityDetailleShopBinding

class DetailleShopActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailleShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailleShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun setupViewPager(viewpagershop: ViewPager) {
        val adapter = ViewPagerAdapter(this.supportFragmentManager)
        adapter.addFragment(TabsHomeFragment(), "Accueil")
        adapter.addFragment(TabsHomeFragment(), "Produits")
        adapter.addFragment(TabsHomeFragment(), "Avis")
        viewpagershop.adapter = adapter

    }
}