package cd.bmduka.com.Utils

import cd.bmduka.com.Model.Boutique
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapHelper(private var googleMap: GoogleMap? = null) {
    fun initMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        //camera type
      //  googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-11.671828362588464, 27.480711936950684), 13f))
    }

    fun centerMapOnShop(shop: Boutique) {
        val latLng = LatLng(shop.coordonne.latitude, shop.coordonne.longitude)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        googleMap?.addMarker(MarkerOptions().position(latLng).title(shop.nom_complet))
    }
}