package br.com.raveline.coroutineskotlin.repository

import br.com.raveline.coroutineskotlin.model.UserModel
import kotlinx.coroutines.delay

class UserRepository {
    suspend fun getUsers(): List<UserModel> {
        delay(8000)
        val users: List<UserModel> = listOf(
            UserModel(1, "Raveline"),
            UserModel(2, "Suelma"),
            UserModel(3, "Bianca"),
            UserModel(4, "Marina")
        )

        return users
    }
}