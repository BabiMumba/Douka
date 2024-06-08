package cd.bmduka.com.Model

data class Produit(
    val nom: String,
    val date:String,
    val description: String,
    val id_product: String,
    val id_boutique: String,
    val categorie:String,
    val sousCategorie: String,
    val nbvue: Int,
    val prix: Int,
    val nbquantite: Int,
    val liste_images: List<String>
)
{
    constructor():this("","","","","","","",0,0,0, emptyList())
}