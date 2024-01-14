package com.ricardomuniz.whichcatits.data.model

data class Cat(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
) {
    data class CatDetail(
        var id: String,
        var temperament: String?,
        var origin: String?,
        var lifeSpan: String?,
        var wikipediaUrl: String?,
    )
}
