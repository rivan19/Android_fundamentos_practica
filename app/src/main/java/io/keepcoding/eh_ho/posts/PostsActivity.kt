package io.keepcoding.eh_ho.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.isFirsTimeCreated
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_topics.view.*

const val EXTRA_TOPIC_ID = "TOPIC_ID"
const val TRANSACTION_LOAD_POSTS = "load_posts"
const val ARG_ID = "id"

class PostsActivity : AppCompatActivity(),
PostsFragment.PostsInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        val topicId: String = intent.getStringExtra(EXTRA_TOPIC_ID) ?: ""
        //val topic: Topic? = TopicsRepo.getTopic(topicId)

        if (isFirsTimeCreated(savedInstanceState)){
            val postFragment = PostsFragment()

            postFragment.arguments?.putString(ARG_ID, topicId)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PostsFragment.newInstance(topicId))
                .commit()
        }

    }

    override fun enableLoading(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.listPosts.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.listPosts.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    override fun onError() {

    }

    override fun onCreateTopic() {

    }
}