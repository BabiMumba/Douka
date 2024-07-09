package cd.bmduka.com.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        var categorie = arguments?.getString("categorie").toString()
        Toast.makeText(requireActivity(), "$categorie", Toast.LENGTH_SHORT).show()

        if (categorie=="null"){
            categorie = ""
        }
        if (categorie.isNotEmpty()){
            binding.toolbarSearch.homeSearchEditText.hint = "Rechercher dans $categorie"
        }else{
            binding.toolbarSearch.homeSearchEditText.hint = "Rechercher un produit"
        }
        getData(categorie)

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return binding.root
    }
    fun getData(categorie: String = ""){
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
                        if (categorie.isNotEmpty()) {
                            if (produit.categorie == categorie) {
                                liste_product.add(produit)
                            }
                        } else {
                            liste_product.add(produit)
                        }
                    }else{
                        Log.d("TAG", "onDataChange: Produit est null")

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