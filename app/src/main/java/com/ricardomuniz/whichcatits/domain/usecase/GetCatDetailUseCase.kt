package com.ricardomuniz.whichcatits.domain.usecase

import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.data.repository.CatRepository
import retrofit2.Response

class GetCatDetailUseCase(private val catRepository: CatRepository) {
    suspend operator fun invoke(id: String) : Response<Cat> {
        return catRepository.getCatDetail(id)
    }
}