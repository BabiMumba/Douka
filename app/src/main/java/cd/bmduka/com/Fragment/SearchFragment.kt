package cd.bmduka.com.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import cd.bmduka.com.Adapter.FilterProduct
import cd.bmduka.com.Model.Produit
import cd.bmduka.com.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        getData()


        return binding.root
    }
    fun getData(){
        val filtres = FilterProduct()
        val liste_product = ArrayList<Produit>()
        val ref = FirebaseDatabase.getInstance().getReference("Produits")
        ref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                liste_product.clear() // Assurez-vous de vider la liste avant de la remplir à nouveau
                for (snap in snapshot.children) {
                    val produit = snap.getValue(Produit::class.java)
                    if (produit != null) {
                        liste_product.add(produit)
                    }
                }
                filtres.items = liste_product
                binding.recyclerProduct.adapter = filtres
                binding.recyclerProduct.adapter?.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })
        binding.toolbarSearch.homeSearchEditText.addTextChangedListener {
            filtres.filter.filter(it)
        }

    }

}