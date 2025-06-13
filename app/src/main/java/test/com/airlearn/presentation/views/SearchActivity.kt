package test.com.airlearn.presentation.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appwise.com.myapplication.R
import test.com.airlearn.presentation.adapters.UserAdapter
import test.com.airlearn.viewmodels.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var clearSearchButton: ImageView
    private lateinit var recyclerView: RecyclerView

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        observeViewModel()
        setupSearchListener()
    }

    private fun initViews() {
        searchEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        clearSearchButton = findViewById(R.id.clearSearchButton)
        recyclerView = findViewById(R.id.recyclerView)

        clearSearchButton.setOnClickListener {
            searchEditText.text.clear()
            clearUI()
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(onUserClick = { user ->
            startActivity(ProfileActivity.newIntent(this, user.username))
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = userAdapter
        }
    }

    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                clearSearchButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
                if (query.isBlank())
                    clearUI()
                else
                    viewModel.searchUsers(query)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                errorTextView.visibility = View.GONE
                recyclerView.visibility = View.GONE
            }
        }

        viewModel.searchResults.observe(this) { users ->
            progressBar.visibility = View.GONE
            if (users != null && users.isNotEmpty()) {
                errorTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                userAdapter.updateUsers(users)
                recyclerView.scrollToPosition(0)
            } else {
                showError(getString(R.string.no_users_found))
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrBlank())
                showError(message)
        }
    }

    private fun showError(message: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = message
    }

    private fun hideError() {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE
    }


    private fun clearUI() {
        userAdapter.updateUsers(emptyList())
        errorTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}
