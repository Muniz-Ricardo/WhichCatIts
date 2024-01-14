package com.ricardomuniz.whichcatits.domain.usecase

import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.data.repository.CatRepository
import retrofit2.Response

class GetCatMoreListUseCase(private val catRepository: CatRepository) {
    suspend operator fun invoke(limit: Int, page: Int): Response<ArrayList<Cat>> {
        return catRepository.getCatList(limit, page)
    }
}