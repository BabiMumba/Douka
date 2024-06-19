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
import com.google.android.material.tabs.TabLayout

class DetailleShopActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailleShopBinding
    private var tabs: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDetailleShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        applyTheme()
        setupViewPager(binding.viewPager)
        tabs = findViewById(R.id.tabLayout)
        tabs!!.setupWithViewPager(binding.viewPager)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        //le tabs n2 est selectionné par defaut
        tabs!!.getTabAt(1)!!.select()//permet de selectionner un tab par defaut


    }
    fun setupViewPager(viewpagershop: ViewPager) {
        val adapter = ViewPagerAdapter(this.supportFragmentManager)
        adapter.addFragment(TabsHomeFragment(), "Info")
        adapter.addFragment(TabsHomeFragment(), "Accueil")
        adapter.addFragment(TabsHomeFragment(), "Avis")
        viewpagershop.adapter = adapter
    }

    //fonction pour appliquer la couleur du theme
    fun applyTheme() {
        val theme = intent.getStringExtra("theme")
        //la couleur du theme est au format #0051FF
        if (theme != null) {
            binding.toolbar.setBackgroundColor(android.graphics.Color.parseColor(theme))
            binding.tabLayout.setBackgroundColor(android.graphics.Color.parseColor(theme))
            //status bar color
            window.statusBarColor = android.graphics.Color.parseColor(theme)
        }
    }
}