package test.com.airlearn.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import test.com.airlearn.data.model.GitHubRepository
import test.com.airlearn.data.model.GitHubUser
import test.com.airlearn.data.repo.GitHubDataRepository

class SearchViewModel : ViewModel() {
    private val repository = GitHubDataRepository()
    val searchResults = MutableLiveData<List<GitHubUser>>()
    val userDetails = MutableLiveData<GitHubUser>()
    val userRepositories = MutableLiveData<List<GitHubRepository>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private var searchJob: Job? = null
    private val debouncePeriod = 500L

    fun searchUsers(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.isNotBlank()) {
                isLoading.value = true
                val users = repository.searchUsers(query)
                if (users != null) {
                    searchResults.value = users
                } else {
                    searchResults.value = emptyList()
                    errorMessage.value = "No users found or error occurred."
                }
                isLoading.value = false
            } else {
                searchResults.value = emptyList()
            }
        }
    }

    fun getUserDetails(username: String) {
        viewModelScope.launch {
            val user = repository.getUserDetails(username)
            if (user != null) {
                userDetails.value = user
            } else {
                errorMessage.value = "Failed to fetch user details."
            }
        }
    }

    fun getUserRepositories(username: String) {
        viewModelScope.launch {
            isLoading.value = true
            val repos = repository.getUserRepositories(username)
            if (repos != null) {
                userRepositories.value = repos
            } else {
                errorMessage.value = "Failed to fetch repositories."
            }
            isLoading.value = false
        }
    }
}
