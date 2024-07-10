package cd.bmduka.com.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import cd.bmduka.com.Adapter.CategorieHomeAdapter
import cd.bmduka.com.Model.Filtre
import cd.bmduka.com.Adapter.FiltreAdapter
import cd.bmduka.com.Adapter.ProduitAdapter
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.Model.homecategorie
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddShopFragment
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
        init_categories()
        initFilter()
        initProduct("id_product")
        binding.searchLayout.setOnClickListener {
            val fragment = SearchFragment()
            Utils.loadfragemnt(requireActivity(),fragment)
        }
        binding.filterBtn.setOnClickListener {
            val fragment = SearchFragment()
            Utils.loadfragemnt(requireActivity(),fragment)
        }


        return binding.root
    }

    fun init_categories(){
        val items = ArrayList<homecategorie>()
        items.add(homecategorie("vetement",1,"https://cdn-icons-png.flaticon.com/128/3345/3345358.png"))
        items.add(homecategorie("chaussure",2,"https://cdn-icons-png.flaticon.com/128/4598/4598559.png"))
        items.add(homecategorie("sac",3,"https://cdn-icons-png.flaticon.com/128/3345/3345654.png"))
        items.add(homecategorie("montre",4,"https://cdn-icons-png.flaticon.com/128/3549/3549067.png"))
        items.add(homecategorie("electronique",8,"https://cdn-icons-png.flaticon.com/128/3659/3659900.png"))
        items.add(homecategorie("telephone",9,"https://cdn-icons-png.flaticon.com/128/519/519184.png"))
        items.add(homecategorie("ordinateur",10,"https://cdn-icons-png.flaticon.com/128/9380/9380459.png"))
        items.add(homecategorie("maison",11,"https://cdn-icons-png.flaticon.com/128/7423/7423011.png"))
        items.add(homecategorie("jardin",12,"https://cdn-icons-png.flaticon.com/128/4145/4145840.png"))
        items.add(homecategorie("sport",13,"https://cdn-icons-png.flaticon.com/128/4163/4163679.png"))
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
        liste_filtre.add(Filtre("Prix",false))

        val myadapter = FiltreAdapter(liste_filtre)
        binding.recyclerFilter.apply {
            adapter = myadapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
        binding.recyclerFilter.adapter?.notifyDataSetChanged()
        myadapter.setOnItemClickListener(object : FiltreAdapter.OnItemClickListener {
            override fun onItemClick(item: Filtre) {
                for (i in liste_filtre){
                    i.isSelected = false
                }
                item.isSelected = true
                myadapter.notifyDataSetChanged()
                when(item.nom){
                    "Tous" -> initProduct("id_product")
                    "Recement Ajouter" -> initProduct("date")
                    "Prix" -> initProduct("prix")
                }
            }
        })

    }

    fun initProduct(trier:String){
        val liste_product = ArrayList<Produit>()
        val ref = FirebaseDatabase.getInstance().getReference("Produits").orderByChild(trier)
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