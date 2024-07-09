package cd.bmduka.com.Fragment

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.bmduka.com.Adapter.ShopAdapter
import cd.bmduka.com.Adapter.ShopCartAdapter
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Model.SliderModel
import cd.bmduka.com.R
import cd.bmduka.com.Utils.MapHelper
import cd.bmduka.com.Utils.ShopRepository
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.View.AddShopFragment
import cd.bmduka.com.ViewModel.MainViewModel
import cd.bmduka.com.ViewModel.ShopViewModel
import cd.bmduka.com.databinding.FragmentShopBinding
import cd.bmduka.com.databinding.MarketFragmentBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopFragment : Fragment(), OnMapReadyCallback {
    private val shopRepository = ShopRepository()
    private val mapHelper = MapHelper()
    private val viewModel = ShopViewModel(shopRepository, mapHelper)

    lateinit var binding:MarketFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MarketFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.shopList.observe(viewLifecycleOwner) { shops ->
            val adapter = ShopCartAdapter(shops)
            binding.recycleShop.adapter = adapter
            adapter.setOnItemClickListener(object : ShopAdapter.OnItemClickListener {
                override fun onItemClick(item: Boutique) {
                    viewModel.centerMapOnShop(item)
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.initMap(googleMap)
        viewModel.fetchShops()
    }
}