package cd.bmduka.com.Model

import com.google.firebase.Timestamp

data class Boutique(
    var id_boutique:String="",
    val nom_complet: String="",
    val description: String="",
    val adresse: String="",
    val nume_appel: String,
    val id_admin:String,
    val logo_btq: String="1",
    val date: String,
    val theme_color: String="#ff4747",
){
    constructor():this("","","","","", "", "", "", "")

}