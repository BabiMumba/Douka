package cd.bmduka.com.Fragment.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.R
import cd.bmduka.com.databinding.FragmentTabsHomeBinding

class TabsHomeFragment : Fragment() {
    lateinit var binding: FragmentTabsHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabsHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


}