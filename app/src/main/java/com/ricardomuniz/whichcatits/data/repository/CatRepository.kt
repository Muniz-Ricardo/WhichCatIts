package com.ricardomuniz.whichcatits.data.repository

import com.ricardomuniz.whichcatits.data.model.Cat
import retrofit2.Response

interface CatRepository {
    suspend fun getCatList(limit: Int, page: Int): Response<ArrayList<Cat>>
    suspend fun getCatDetail(id: String): Response<Cat>
}