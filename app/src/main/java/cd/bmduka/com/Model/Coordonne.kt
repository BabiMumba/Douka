package cd.bmduka.com.Model

data class Coordonne(
    val latitude: Double,
    val longitude: Double,
    val adresse: String,

){
    constructor():this(0.0,0.0,"")
}