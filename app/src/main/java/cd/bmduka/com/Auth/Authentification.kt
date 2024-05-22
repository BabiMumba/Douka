package cd.bmduka.com.Auth

import android.util.Log
import cd.bmduka.com.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

object Authentification {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    //fonction pour verifier si l'utilisateur est connecté
    fun isUserConnected(user: FirebaseUser?): Boolean {
        return user != null
    }

    fun createUserWithEmail(email: String, password: String, name: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                uid?.let {
                    val user = User(it, name, email, password)
                    db.collection("users").document(it).set(user)
                }
            }
        }
    }
    fun signInWithGoogle(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
    }
    fun loginWithEmail(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }
}