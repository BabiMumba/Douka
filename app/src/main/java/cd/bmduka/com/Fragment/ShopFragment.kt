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
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddShopFragment
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentShopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopFragment : Fragment() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    lateinit var binding:FragmentShopBinding
    val viewModel = MainViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(layoutInflater)
        getliste()
        Panel()
        binding.msgCreatebtq.createShopBtn.setOnClickListener {
            // Ouvrir le fragment d'ajout de boutique
            val fragment = AddShopFragment()
            Utils.loadfragemnt(requireActivity(),fragment)
        }
        btque()
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

    fun Panel(){
        val isSeller= Utils.IsVendeur(requireActivity())
        if (isSeller) {
            binding.msgCreatebtq.root.visibility = View.GONE
            binding.shopOwn.root.visibility = View.VISIBLE
        }else{
            binding.msgCreatebtq.root.visibility = View.VISIBLE
            binding.shopOwn.root.visibility = View.GONE
        }
    }

    fun btque(){
        val ui= Utils.getUID(viewModel.GetmailUser())
       viewModel.fetchBoutique(ui){btq->
          Log.d("btqur_id",btq.id_admin)
       }
    }


    override fun onResume() {
        super.onResume()
        Panel()
    }



}