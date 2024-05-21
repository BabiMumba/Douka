package cd.bmduka.com.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.R
import cd.bmduka.com.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}