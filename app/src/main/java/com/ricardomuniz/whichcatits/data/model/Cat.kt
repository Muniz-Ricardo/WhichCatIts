package com.ricardomuniz.whichcatits.data.model

data class Cat(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
) {
    data class Detail(
        var breeds: ArrayList<Breeds>?
    )

    data class Breeds(
        var id: String,
        var name: String?,
        var temperament: String?,
        var origin: String?,
        var lifeSpan: String?,
        var wikipediaUrl: String?,
    )
}
