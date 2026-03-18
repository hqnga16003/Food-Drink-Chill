package com.example.fooddrinkchill.data.repository


import com.example.fooddrinkchill.data.model.Result
import com.example.fooddrinkchill.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseRepository : BaseRepository() {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {

            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            val firebaseUser = result.user
                ?: return Result.Error("Đăng nhập thất bại")
            Result.Success(firebaseUser.toUser())

        } catch (e: FirebaseAuthInvalidCredentialsException) {

            Result.Error("Sai mật khẩu hoặc tài khoản")

        } catch (e: Exception) {

            Result.Error("Đăng nhập thất bại", e)

        }
    }

    override suspend fun register(email: String, password: String):
            Result<User> {
        return try {
            val result = auth
                .createUserWithEmailAndPassword(email.trim().lowercase(), password)
                .await()
            val firebaseUser = result.user
                ?: return Result.Error("Lỗi đăng ký")
            firebaseUser.sendEmailVerification()

            Result.Success(firebaseUser.toUser())
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.Error(e.reason.toString(), e)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error("Email đã được đăng ký", e)
        } catch (e: Exception) {
            Result.Error("Lỗi đăng ký", e)
        }
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            id = uid,
            email = email,
        )
    }
}
