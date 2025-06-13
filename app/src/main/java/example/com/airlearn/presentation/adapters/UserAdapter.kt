package example.com.airlearn.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import example.com.airlearn.data.model.GitHubUser
import appwise.com.myapplication.R
import com.bumptech.glide.Glide

class UserAdapter(
    private val users: MutableList<GitHubUser> = mutableListOf(),
    private val onUserClick: (GitHubUser) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView = itemView.findViewById(R.id.avatarImageView)
        private val username: TextView = itemView.findViewById(R.id.usernameTextView)

        fun bind(user: GitHubUser) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_place_holder)
                .circleCrop()
                .into(avatar)

            username.text = user.username
            itemView.setOnClickListener { onUserClick(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<GitHubUser>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}