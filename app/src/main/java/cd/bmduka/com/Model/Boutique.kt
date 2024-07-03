package cd.bmduka.com.Model

data class Boutique(
    var id_boutique: String ="",
    val nom_complet: String="",
    val description: String="",
    val nume_appel: String,
    val id_admin:String,
    val logo_btq: String="1",
    val date: String,
    val theme_color: String="#ff4747",
    val coordonne: Coordonne,
){
    constructor():this("","","","","","","",coordonne=Coordonne())
}
