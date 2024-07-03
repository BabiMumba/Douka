package cd.bmduka.com.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cd.bmduka.com.MainActivity
import cd.bmduka.com.Model.User
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import cd.bmduka.com.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    //show toast
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    //new intent
    fun newIntent(context: Context, activity: Class<*>) {
        context.startActivity(Intent(context, activity))
    }
    //new intent with extra
    fun newIntentWithExtra(context: Context, activity: Class<*>, key: String, value: String,id_boutique:String,value_boutique:String) {
        val intent = Intent(context, activity)
        intent.putExtra(key, value)
        intent.putExtra(id_boutique,value_boutique)
        context.startActivity(intent)
    }
    //new intent finish
    fun newIntentFinish(context: Context, activity: Class<*>) {
        context.startActivity(Intent(context, activity))
        (context as android.app.Activity).finish()
    }
    fun isloading(button:View,progressBar: CircularProgressIndicator, isLoading: Boolean) {
        if (isLoading) {
            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            button.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

    }
    fun isloadingFrame(frame:View, isLoading: Boolean) {
        if (isLoading) {
            frame.visibility = View.GONE
        } else {
            frame.visibility = View.VISIBLE
        }

    }
    fun username(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("name","").toString()
    }
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun getCurrentDate(): Timestamp {
        return Timestamp.now()
    }
    //function pour retourner la date au format 12/12/2021
    fun formatDate(date: Timestamp): String {
        val date = date.toDate()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun getUID(mail:String):String{
        var id_user = mail.substringBefore("@").toString()
        //retirer les points et les caracteres speciaux
        for (i in id_user.indices){
            if (id_user[i] == '.' || id_user[i] == '#' || id_user[i] == '$' || id_user[i] == '[' || id_user[i] == ']'){
                id_user = id_user.replace(id_user[i].toString(),"")
            }
        }

        return id_user
    }
    fun savename(context: Context,name_user:String){
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",name_user)
        editor.apply()
    }
    fun IsVendeur(context: Context):Boolean{
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isVendeur",false)
    }

    fun saveVendeur(context: Context,isVendeur:Boolean){
        val sharedPreferences = context.getSharedPreferences(DATA.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isVendeur",isVendeur)
        editor.apply()
    }

    fun loadfragemnt(context: Context,fragment: Fragment){
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.nav_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun SaveUserToFirestore(user: User,context: Context){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    savename(context,user.name)
                    newIntentFinish(context, MainActivity::class.java)
                }
            }
    }

    class LocationUtils(private val context: Context) {
        private val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        private val REQUEST_CODE_LOCATION_PERMISSION = 123

        fun getCurrentLocation(onLocationRetrieved: (latitude: Double, longitude: Double) -> Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        onLocationRetrieved(location.latitude, location.longitude)
                    }
                }
            } else {
                requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION
                )
            }
        }

        private fun requestPermissions(
            activity: Activity,
            permissions: Array<String>,
            requestCode: Int
        ) {
            activity.requestPermissions(permissions, requestCode)
        }
    }


}