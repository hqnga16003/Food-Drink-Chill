package com.example.fooddrinkchill.data.repository

import com.example.fooddrinkchill.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    protected suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(call())
            } catch (e: Exception) {
                Result.Error(e.message ?: "An unknown error occurred", e)
            }
        }
    }
    abstract suspend fun login()
    abstract suspend fun register()

}
