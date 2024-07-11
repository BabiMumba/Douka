package cd.bmduka.com.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cd.bmduka.com.R
import cd.bmduka.com.Utils.DATA
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddShopFragment
import cd.bmduka.com.View.EditProfileActivity
import cd.bmduka.com.View.SplashActivity
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        mainViewModel = ViewModelProvider(activity)[MainViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mail = mainViewModel.GetmailUser()

        if (Utils.IsConnected(requireContext())){
            binding.userMail.text = mail
        }else{
            binding.userMail.text = "Veuillez vous connecter"
        }
        binding.logout.setOnClickListener {
            logout()
        }
        binding.managerShop.setOnClickListener {
            // Ouvrir le fragment d'ajout de boutique
           /* val fragment = AddShopFragment()
            Utils.loadfragemnt(requireActivity(),fragment)*/
        }
        val sharepref = context?.getSharedPreferences(DATA.PREF_NAME,Context.MODE_PRIVATE)
        val name = sharepref!!.getString("name","")
        binding.userName.text = name

        binding.edtProfileBtn.setOnClickListener {
            if (Utils.IsConnected(requireContext())){
                Utils.newIntent(requireActivity(),EditProfileActivity::class.java)
            }else{
                Utils.showToast(requireContext(),"Veuillez vous connecter")
            }
        }


    }

    fun logout(){
        val dialogue = AlertDialog.Builder(requireContext())
        dialogue.setTitle("Deconnexion")
        dialogue.setMessage("Voulez-vous vraiment vous deconnecter ?")
        dialogue.setPositiveButton("Oui") { dialog, which ->
            //deconnexion
            mainViewModel.logout(requireActivity())
            Utils.newIntent(requireActivity(),SplashActivity::class.java)
            requireActivity().finish()
        }
        dialogue.setNegativeButton("Non") { dialog, which ->
            //ne rien faire
            dialog.dismiss()
        }
        dialogue.show()
    }

    override fun onResume() {
        val name = Utils.username(requireContext())
        binding.userName.text = name
        super.onResume()
    }



}