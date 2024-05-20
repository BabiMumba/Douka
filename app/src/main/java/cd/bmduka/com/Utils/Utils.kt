package cd.bmduka.com.Utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

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
}