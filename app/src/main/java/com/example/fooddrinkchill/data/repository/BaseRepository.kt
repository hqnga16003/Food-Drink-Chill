package com.example.fooddrinkchill.data.repository

import com.example.fooddrinkchill.data.model.Result
import com.example.fooddrinkchill.data.model.User
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
    abstract suspend fun login(email: String, password: String): Result<User>
    abstract suspend fun register(email: String, password: String): Result<User>

}
