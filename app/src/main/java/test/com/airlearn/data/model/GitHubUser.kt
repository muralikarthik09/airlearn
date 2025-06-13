package test.com.airlearn.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    val bio: String?,
    val followers: Int,
    @SerializedName("public_repos") val publicRepos: Int
)
data class GitHubRepository(
    val name: String,
    val description: String?,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("forks_count") val forks: Int
)

data class GitHubUserSearchResponse(
    @SerializedName("items") val users: List<GitHubUser>
)
