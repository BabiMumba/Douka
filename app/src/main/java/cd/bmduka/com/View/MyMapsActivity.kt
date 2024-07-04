package cd.bmduka.com.View

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cd.bmduka.com.Model.Boutique
import cd.bmduka.com.Model.Coordonne
import cd.bmduka.com.R
import cd.bmduka.com.Utils.Utils
import cd.bmduka.com.databinding.ActivityMyMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyMapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CODE_LOCATION_PERMISSION = 123
    private lateinit var mLastLocation: Location
    private var marker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var binding: ActivityMyMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.selectAd.setOnClickListener {
            handleValidation()
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-11.671828362588464, 27.480711936950684), 13f))
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient()
            mMap?.setOnMapClickListener { latLng ->
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap?.animateCamera(CameraUpdateFactory.zoomTo(18f))
                handleMapClick(latLng)
                binding.selectAd.visibility = View.VISIBLE
                latitude = latLng.latitude
                longitude= latLng.longitude
            }
            mMap?.isMyLocationEnabled = true
        } else {
            // Request permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }
    private fun handleMapClick(latLng: LatLng) {
        // Supprimez le marqueur précédent s'il existe
        marker?.remove()
        // Créez un nouveau marqueur à l'emplacement cliqué
        marker = mMap!!.addMarker(MarkerOptions().position(latLng))
        // Affichez les coordonnées dans les logs
        Log.d("MapsActivity", "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")
    }
    private fun handleValidation() {
        // Vérifiez si un marqueur a été placé
        marker?.let { m ->
            // Récupérez les coordonnées du marqueur
            val latitude = m.position.latitude
            val longitude = m.position.longitude
            //recuperer les intents
            val intent = intent
            val id_boutique = intent.getStringExtra("id_boutique")
            val nom_complet = intent.getStringExtra("nom_complet")
            val description = intent.getStringExtra("description")
            val nume_appel = intent.getStringExtra("nume_appel")
            val id_admin = intent.getStringExtra("id_admin")
            val logo_btq = intent.getStringExtra("logo_btq")
            val date = intent.getStringExtra("date")
            val theme_color = intent.getStringExtra("theme_color")
            val adresse = intent.getStringExtra("adresse").toString()
            val coordonne = Coordonne(latitude, longitude,adresse)
            val shop = Boutique(
                id_boutique!!,
                nom_complet!!,
                description!!,
                nume_appel!!,
                id_admin!!,
                logo_btq!!,
                date!!,
                theme_color!!,
                coordonne
            )
            add_shop(shop)


        } ?: run {
            Log.d("MapsActivity", "Veuillez choisir un emplacement sur la carte.")
        }
    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        marker?.remove()

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Current Position")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        marker = mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }
    fun add_shop(shop: Boutique){
        Utils.isloadingFrame(binding.loaderFrameLayout.loaderFrameLayout,true)
        val ref = FirebaseDatabase.getInstance().getReference("Boutique")
        val id = ref.push().key.toString()
        ref.child(id).setValue(shop)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //cela
                    Utils.isloadingFrame(binding.loaderFrameLayout.loaderFrameLayout,false)
                    // Si l'ajout est réussi
                    Utils.showToast(this, "Boutique ajoutée avec succès")
                  //  requireActivity().onBackPressed()
                    Utils.saveVendeur(this,true)
                }else{
                    Utils.isloadingFrame(binding.loaderFrameLayout.loaderFrameLayout,false)
                    Utils.showToast(this, "Erreur: ${it.exception?.message}")
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
            .setInterval(1000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
        }

    }


    override fun onConnectionSuspended(p0: Int) {
        Utils.showToast(this, "Connection Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Utils.showToast(this, "Connection Failed")
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap?.apply {
                        addMarker(MarkerOptions().position(currentLocation).title("Votre position"))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20f))
                    }
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }
}