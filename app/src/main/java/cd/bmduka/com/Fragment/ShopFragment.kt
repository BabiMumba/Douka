package cd.bmduka.com.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Adapter.ShopAdapter
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.R
import cd.bmduka.com.View.AddShopFragment
import cd.bmduka.com.databinding.FragmentShopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopFragment : Fragment() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    lateinit var binding:FragmentShopBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(layoutInflater)
        getliste()
        binding.msgCreatebtq.createShopBtn.setOnClickListener {
            // Ouvrir le fragment d'ajout de boutique
            val fragment = AddShopFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.nav_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    // Fonction pour récupérer la liste des boutiques depuis Firebase
    fun getliste() {
        val ref = firebaseDatabase.getReference("Boutique")
        val listes = ArrayList<Boutique>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listes.clear()
                for (snap in snapshot.children) {
                    val boutique = snap.getValue(Boutique::class.java)
                    if (boutique != null) {
                        listes.add(boutique)
                        Log.d("boutique", boutique.toString())
                    }
                }
                binding.shopRec.adapter = ShopAdapter(listes)
                binding.shopRec.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })
    }



}