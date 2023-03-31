package com.example.acronymdefinitionapp.network

import com.example.acronymdefinitionapp.utils.FailureResponse
import com.example.acronymdefinitionapp.utils.NullResponseException
import com.example.acronymdefinitionapp.utils.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AcronymRepository {
    fun getAcronymDefinition(acronym: String): Flow<RequestState>
}

class AcronymRepositoryImpl(
    private val acronymService: AcronymService = AcronymService.acronymService
) : AcronymRepository {

    override fun getAcronymDefinition(acronym: String): Flow<RequestState> = flow {
        emit(RequestState.LOADING())

        try {
            val response = acronymService.getAcronymDefinition(acronym)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(RequestState.SUCCESS(it))
                } ?: throw NullResponseException("Acronym response is null")
            } else {
                throw FailureResponse("Acronym definition response is a failure")
            }
        } catch (e: Exception) {
            emit(RequestState.ERROR(e))
        }
    }
}