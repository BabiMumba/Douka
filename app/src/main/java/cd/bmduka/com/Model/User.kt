package cd.bmduka.com.Model

import com.google.firebase.Timestamp
import com.google.type.DateTime

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val password: String,
    val DateInscription: Timestamp,
)
{
    constructor() : this("", "", "", "", Timestamp.now())

}