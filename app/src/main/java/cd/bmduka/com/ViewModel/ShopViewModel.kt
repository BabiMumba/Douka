package cd.bmduka.com.ViewModel

import androidx.lifecycle.MutableLiveData
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Utils.MapHelper
import cd.bmduka.com.Utils.ShopRepository
import com.google.android.gms.maps.GoogleMap

class ShopViewModel(
    private val shopRepository: ShopRepository,
    private val mapHelper: MapHelper
) {
    val shopList = MutableLiveData<ArrayList<Boutique>>()

    fun fetchShops() {
        shopRepository.getShops { shops ->
            shopList.postValue(shops)
        }
    }

    fun centerMapOnShop(shop: Boutique) {
        mapHelper.centerMapOnShop(shop)
    }

    fun initMap(googleMap: GoogleMap) {
        mapHelper.initMap(googleMap)
    }
}