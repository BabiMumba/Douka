package cd.bmduka.com

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class MyApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
       //

    }

    override fun onCreate() {
        super.onCreate()
        Locale.setDefault(Locale("fr"))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}