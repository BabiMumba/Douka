package cd.bmduka.com.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentAddShopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddShopFragment : Fragment() {
    lateinit var binding: FragmentAddShopBinding
    var lastid = ""
    var lastIdBtque=0
    //maiviewmodel
    val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShopBinding.inflate(layoutInflater)
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        getLastId {
            lastid = it
        }



        val mail_user = viewModel.GetmailUser()
        val id_prop = Utils.getUID(mail_user)

        val id_boutique = id_prop + lastIdBtque


        binding.btnSave.setOnClickListener {
            if (checkFields()) {
                    val shop = Boutique(
                        id_boutique,
                        binding.shopName.text.toString(),
                        binding.edtDescription.text.toString(),
                        binding.edtAddress.text.toString(),
                        binding.edtPhone.text.toString(),
                        id_prop,
                        "1",
                        "05/05/2020",

                        )
                    add_shop(shop)
            }else{
                Utils.showToast(requireContext(), "Veuillez remplir tous les champs")
            }
        }
        return binding.root
    }
    fun add_shop(shop:Boutique){
        Utils.isloading(binding.btnSave,binding.progress,true)
        // Ajouter une boutique dans la base de données
        val ref = FirebaseDatabase.getInstance().getReference("Boutique")
        val id = ref.push().key.toString()
        ref.child(id).setValue(shop)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Utils.isloading(binding.btnSave,binding.progress,false)
                    // Si l'ajout est réussi
                    Utils.showToast(requireContext(), "Boutique ajoutée avec succès")
                }else{
                    Utils.isloading(binding.btnSave,binding.progress,false)
                    Utils.showToast(requireContext(), "Erreur: ${it.exception?.message}")
                }

            }
    }
    private fun checkFields(): Boolean {
        if (binding.shopName.text.toString().isEmpty()) {
            binding.shopName.error = "Nom de la boutique est requis"
            return false
        }
        if (binding.edtDescription.text.toString().isEmpty()) {
            binding.edtDescription.error = "Description de la boutique est requise"
            return false
        }
        if (binding.edtAddress.text.toString().isEmpty()) {
            binding.edtAddress.error = "Adresse de la boutique est requise"
            return false
        }
        if (binding.edtPhone.text.toString().isEmpty()) {
            binding.edtPhone.error = "Numéro de téléphone de la boutique est requis"
            return false
        }
        return true
    }

    fun getLastId(callback: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Boutique")
        val liste_elements = ArrayList<Boutique>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val boutique = snap.getValue(Boutique::class.java)
                    if (boutique != null) {
                        liste_elements.add(boutique)
                    }
                }
                val id = liste_elements[liste_elements.size - 1].id_boutique
                callback(id)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })
    }

}