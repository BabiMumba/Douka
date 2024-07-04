package cd.bmduka.com.Utils

import android.util.Log
import cd.bmduka.com.Model.Boutique
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun getShops(onShopsLoaded: (ArrayList<Boutique>) -> Unit) {
        val ref = firebaseDatabase.getReference("Boutique")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val shops = mutableListOf<Boutique>()
                for (snap in snapshot.children) {
                    val shop = snap.getValue(Boutique::class.java)
                    if (shop != null) {
                        shops.add(shop)
                    }
                }
                onShopsLoaded(shops!! as ArrayList<Boutique>)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: $error")
            }
        })
    }
}