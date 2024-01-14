package com.ricardomuniz.whichcatits.data.repository

import com.ricardomuniz.whichcatits.data.model.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getCatList(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<ArrayList<Cat>>

    @GET("v1/images/{id}")
    suspend fun getCatDetail(@Path("id") catId: String): Response<ArrayList<Cat.CatDetail>>
}