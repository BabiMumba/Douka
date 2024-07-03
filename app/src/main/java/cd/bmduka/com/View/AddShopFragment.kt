package cd.bmduka.com.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Model.Coordonne
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentAddShopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddShopFragment : Fragment() {
    lateinit var binding: FragmentAddShopBinding
    var lastid = 0
    //maiviewmodel
    val viewModel = MainViewModel()
    var latitud = 0.0
    var longitud = 0.0

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

        binding.btnSave.setOnClickListener {

            if (checkFields()) {
                val adresse = binding.edtAddress.text.toString()
                //verifier si les coordonnees sont vides
                val coordonne =Coordonne(latitud, longitud,adresse)
                val id_boutique = "$id_prop-$lastid"
                val datetm = Utils.getCurrentDate()
                val datejour = Utils.formatDate(datetm)
                checkIfShopExists(id_prop) { exists ->
                    if (!exists) {
                        val shop = Boutique(
                            id_boutique,
                            binding.shopName.text.toString(),
                            binding.edtDescription.text.toString(),
                            binding.edtPhone.text.toString(),
                            id_prop,
                            "1",
                            datejour,
                            "#ff4747",
                            coordonne
                        )
                        //prendre le shop pour aller a la page suivante
                        val intent = Intent(requireContext(), MyMapsActivity::class.java)
                        intent.putExtra("id_boutique", shop.id_boutique)
                        intent.putExtra("nom_complet", shop.nom_complet)
                        intent.putExtra("description", shop.description)
                        intent.putExtra("nume_appel", shop.nume_appel)
                        intent.putExtra("id_admin", shop.id_admin)
                        intent.putExtra("logo_btq", shop.logo_btq)
                        intent.putExtra("date", shop.date)
                        intent.putExtra("theme_color", shop.theme_color)
                        intent.putExtra("adresse", adresse)
                        startActivity(intent)
                    } else {
                        Utils.showToast(requireContext(), "Une boutique avec cet ID utilisateur existe déjà")
                    }
                }
            }
        }

        return binding.root
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
    fun checkIfShopExists(userId: String, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Boutique")
        ref.orderByChild("id_prop").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }
        })
    }

    fun getLastId(callback: (Int) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Boutique")
        val liste_elements = ArrayList<Boutique>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val boutique = snap.getValue(Boutique::class.java)
                    if (boutique != null) {
                        liste_elements.add(boutique)
                    }else{
                        callback(0)
                    }
                }
                /*val id = liste_elements[liste_elements.size - 1].id_boutique
                callback(id)*/
                //si la liste contient des elements on recupere le dernier id
                lastid = if (liste_elements.size > 0) {
                    liste_elements.size+1
                } else {
                    0
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ")
            }
        })
    }

}