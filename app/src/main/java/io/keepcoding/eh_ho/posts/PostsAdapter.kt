package io.keepcoding.eh_ho.posts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.Post
import io.keepcoding.eh_ho.inflate
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostHolder>() {

    private val posts = mutableListOf<Post>()


    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = parent.inflate(R.layout.item_post)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        var post = posts[position]
        holder.post = post
    }

    fun setPosts(posts: List<Post>){
        this.posts.clear()
        notifyDataSetChanged()
        this.posts.addAll(posts)
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var post: Post? = null
        set(value) {
            field = value
            itemView.tag = field

            field?.let {
                itemView.labelPostUsername.text = it.username
                itemView.labelPostContent.text = it.cooked
                itemView.labelPostCreated.text = it.created_at.toString()
            }
        }
    }


}