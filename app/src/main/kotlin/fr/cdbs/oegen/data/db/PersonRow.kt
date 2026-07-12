package fr.cdbs.oegen.data.db

data class PersonRow(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val gender: String?,
    val fatherId: String?,
    val motherId: String?,
    val extraJson: String?,
)
