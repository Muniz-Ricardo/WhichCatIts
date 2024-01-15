package com.ricardomuniz.whichcatits.data.repository

import com.ricardomuniz.whichcatits.data.model.Cat
import retrofit2.Response

class CatRepositoryImpl(private val api: CatApiService) : CatRepository {
    override suspend fun getCatList(limit: Int, page: Int): Response<ArrayList<Cat>> {
        return api.getCatList(limit, page)
    }

    override suspend fun getCatDetail(id: String): Response<ArrayList<Cat.Detail>> {
        return api.getCatDetail(catId = id)
    }
}