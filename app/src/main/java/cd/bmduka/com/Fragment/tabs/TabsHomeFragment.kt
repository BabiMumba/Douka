package cd.bmduka.com.Fragment.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import cd.bmduka.com.Adapter.ProduitAdapter
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.R
import cd.bmduka.com.databinding.FragmentTabsHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TabsHomeFragment : Fragment() {
    lateinit var binding: FragmentTabsHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // binding.nestedScrollView.scrollTo(0, 0)//pour remonter en haut de la page
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setNestedScrollingEnabled(binding.nestedScrollView, false );
        initProduct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabsHomeBinding.inflate(inflater, container, false)
        return binding.root
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
                    setHasFixedSize(true)
                }
                binding.recyclerProduct.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })

    }


}