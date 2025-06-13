package test.com.airlearn.data.services

import test.com.airlearn.data.model.GitHubRepository
import test.com.airlearn.data.model.GitHubUser
import test.com.airlearn.data.model.GitHubUserSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): Response<GitHubUserSearchResponse>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<GitHubUser>

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<GitHubRepository>>
}