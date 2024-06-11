package cd.bmduka.com.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import cd.bmduka.com.Adapter.CategorieHomeAdapter
import cd.bmduka.com.Adapter.SliderAdapter
import cd.bmduka.com.Model.Filtre
import cd.bmduka.com.Adapter.FiltreAdapter
import cd.bmduka.com.Adapter.ProduitAdapter
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.Model.homecategorie
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddShopFragment
import cd.bmduka.com.View.SearchByImageActivity
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var viewModel= MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        initBanner()
        init_categories()
        initFilter()
        initProduct()
        binding.searchLayout.setOnClickListener {
            val fragment = SearchFragment()
            Utils.loadfragemnt(requireActivity(),fragment)
        }
        binding.searchCamera.setOnClickListener {
            Utils.newIntent(requireActivity(),SearchByImageActivity::class.java)
        }


        return binding.root
    }

    fun initBanner(){
        binding.progressBarSlider.visibility = View.VISIBLE
        viewModel.banners.observe(requireActivity()) { items ->
                banners(items)
                binding.progressBarSlider.visibility = View.GONE
        }
        viewModel.loadBanners()

    }
    fun banners(image:List<SliderModel>){
        binding.viewPager.adapter = SliderAdapter(image,binding.viewPager)
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPager.setPageTransformer(compositePageTransformer)

        if (image.size > 1){
            binding.dotsIndicator.visibility = View.VISIBLE
            binding.dotsIndicator.attachTo(binding.viewPager)
        }

    }

    fun init_categories(){
        val items = ArrayList<homecategorie>()
        items.add(homecategorie("vetement",1))
        items.add(homecategorie("chaussure",2))
        items.add(homecategorie("sac",3))
        items.add(homecategorie("montre",4))
        items.add(homecategorie("bijoux",5))
        items.add(homecategorie("parfum",6))
        items.add(homecategorie("cosmetique",7))
        items.add(homecategorie("electronique",8))
        items.add(homecategorie("telephone",9))
        items.add(homecategorie("ordinateur",10))
        items.add(homecategorie("maison",11))
        items.add(homecategorie("jardin",12))
        items.add(homecategorie("sport",13))
        items.add(homecategorie("bebe",14))
        binding.recyclerCategories.apply {
              adapter = CategorieHomeAdapter(items)
           //manager
           layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
       }
        binding.recyclerCategories.adapter?.notifyDataSetChanged()


    }

    fun initFilter(){
        val liste_filtre = ArrayList<Filtre>()
        liste_filtre.add(Filtre("Tous",true))
        liste_filtre.add(Filtre("Recement Ajouter",false))
        liste_filtre.add(Filtre("Recommandation",false))

        binding.recyclerFilter.apply {
            adapter = FiltreAdapter(liste_filtre)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
        binding.recyclerFilter.adapter?.notifyDataSetChanged()

    }

    fun initProduct(){
        val liste_product = ArrayList<Produit>()
        val ref = FirebaseDatabase.getInstance().getReference("Produits")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                liste_product.clear() // Assurez-vous de vider la liste avant de la remplir à nouveau
                for (snap in snapshot.children) {
                    val produit = snap.getValue(Produit::class.java)
                    if (produit != null) {
                        liste_product.add(produit)
                    }
                }
                val mylayoutManager = GridLayoutManager(requireContext(),2)
                binding.recyclerProduct.apply {
                    adapter = ProduitAdapter(liste_product)
                    layoutManager = mylayoutManager
                }
                binding.recyclerProduct.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })

    }


}