package test.com.airlearn.data.repo

import test.com.airlearn.data.model.GitHubRepository
import test.com.airlearn.data.model.GitHubUser
import test.com.airlearn.data.network.ApiClient

class GitHubDataRepository {
    private val apiService = ApiClient.apiService

    suspend fun searchUsers(query: String): List<GitHubUser>? {
        return try {
            val response = apiService.searchUsers(query)
            if (response.isSuccessful) {
                response.body()?.users
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserDetails(username: String): GitHubUser? {
        return try {
            val response = apiService.getUser(username)
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserRepositories(username: String): List<GitHubRepository>? {
        return try {
            val response = apiService.getUserRepositories(username)
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
