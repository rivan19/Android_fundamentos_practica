 package io.keepcoding.eh_ho.topics

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.keepcoding.eh_ho.*
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.data.UserRepo
import io.keepcoding.eh_ho.login.LoginActivity
import io.keepcoding.eh_ho.posts.EXTRA_TOPIC_ID
import io.keepcoding.eh_ho.posts.PostsActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_topics.view.*

 const val TRANSACTION_CREATE_TOPIC = "create_topic"
 const val TRANSACTION_ERROR_LOADING_TOPIC = "error_loading_topic"
 const val TRANSACTION_LOAD_TOPIC = "load_topics"

class TopicsActivity : AppCompatActivity(),
    TopicsFragment.TopicsInteractionListener,
    CreateTopicFragment.CreateTopicInteractionListener,
    TopicsFragmentErrorLoading.TopicsFragmentErrorLoadingListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        if (isFirsTimeCreated(savedInstanceState))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, TopicsFragment())
                .addToBackStack(TRANSACTION_LOAD_TOPIC)
                .commit()


    }

    override fun retry() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TopicsFragment())
            .commit()
        //supportFragmentManager.popBackStack()
    }

    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        startActivity(intent)
    }

    override fun onCreateTopic() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTopicFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    override fun onError() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TopicsFragmentErrorLoading())
            .addToBackStack(TRANSACTION_ERROR_LOADING_TOPIC)
            .commit()
    }

    override fun onShowPosts(topic: Topic) {
        goToPosts(topic)
    }

    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()

    }

    override fun onLogout() {
        //Borrar datos
        UserRepo.logout(this.applicationContext)

        //Ir a actividad inicial
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun enableLoading(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.listTopics.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.listTopics.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }
}