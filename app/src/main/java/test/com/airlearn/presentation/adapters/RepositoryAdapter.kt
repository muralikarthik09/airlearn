package test.com.airlearn.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import test.com.airlearn.data.model.GitHubRepository
import appwise.com.myapplication.R

class RepositoryAdapter(
    private val repositories: MutableList<GitHubRepository> = mutableListOf()
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.repositoryNameTextView)
        private val description: TextView =
            itemView.findViewById(R.id.repositoryDescriptionTextView)
        private val stars: TextView = itemView.findViewById(R.id.starsTextView)
        private val forks: TextView = itemView.findViewById(R.id.forksTextView)

        fun bind(repo: GitHubRepository) {
            name.text = repo.name
            description.text = repo.description ?: "No description"
            stars.text = repo.stars.toString()
            forks.text = repo.forks.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int = repositories.size

    fun updateRepositories(newRepositories: List<GitHubRepository>) {
        repositories.clear()
        repositories.addAll(newRepositories)
        notifyDataSetChanged()
    }
}