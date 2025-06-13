package example.com.airlearn.presentation.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appwise.com.myapplication.R
import com.bumptech.glide.Glide
import example.com.airlearn.data.model.GitHubUser
import example.com.airlearn.presentation.adapters.RepositoryAdapter
import example.com.airlearn.viewmodels.SearchViewModel

class ProfileActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var repositoryAdapter: RepositoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var repositoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username.isNullOrBlank()) {
            Toast.makeText(this, R.string.error_loading_profile, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        setupRecyclerView()
        setupObservers()

        viewModel.getUserDetails(username)
        viewModel.getUserRepositories(username)
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        repositoriesRecyclerView = findViewById(R.id.repositoriesRecyclerView)
    }

    private fun setupRecyclerView() {
        repositoryAdapter = RepositoryAdapter()
        repositoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = repositoryAdapter
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.userDetails.observe(this) { user ->
            if (user != null) {
                displayUserInfo(user)
            } else {
                showErrorAndFinish()
            }
        }

        viewModel.userRepositories.observe(this) { repositories ->
            if (repositories != null && repositories.isNotEmpty()) {
                repositoryAdapter.updateRepositories(repositories)
            } else {
                Toast.makeText(this, R.string.no_repositories_found, Toast.LENGTH_SHORT).show()
                repositoryAdapter.updateRepositories(emptyList())
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayUserInfo(user: GitHubUser) {
        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(findViewById(R.id.avatarImageView))

        findViewById<TextView>(R.id.usernameTextView).text = user.username
        findViewById<TextView>(R.id.bioTextView).text = user.bio ?: getString(R.string.no_bio_available)
        findViewById<TextView>(R.id.followersTextView).text = getString(R.string.followers_count, user.followers)
        findViewById<TextView>(R.id.reposTextView).text = getString(R.string.repositories_count, user.publicRepos)
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, R.string.error_loading_profile, Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        fun newIntent(context: Context, username: String): Intent {
            return Intent(context, ProfileActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
            }
        }
    }
}
