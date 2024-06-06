package cd.bmduka.com.Utils

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

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
    fun newIntentWithExtra(context: Context, activity: Class<*>, key: String, value: String) {
        val intent = Intent(context, activity)
        intent.putExtra(key, value)
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
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun getCurrentDate(): Timestamp {
        return Timestamp.now()
    }

    fun getUID(mail:String):String{

        var id_vendeur = mail.substringBefore("@").toString()
        //retirer les points et les caracteres speciaux
        for (i in id_vendeur.indices){
            if (id_vendeur[i] == '.' || id_vendeur[i] == '#' || id_vendeur[i] == '$' || id_vendeur[i] == '[' || id_vendeur[i] == ']'){
                id_vendeur = id_vendeur.replace(id_vendeur[i].toString(),"")
            }
        }

        return id_vendeur
    }
}