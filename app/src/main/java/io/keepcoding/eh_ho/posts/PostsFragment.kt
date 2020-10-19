package io.keepcoding.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.PostRepo
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.inflate
import io.keepcoding.eh_ho.topics.TopicsFragment
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.buttonCreate
import kotlinx.android.synthetic.main.fragment_topics.*

const val ARG_ID_TOPIC = "idTopic"

class PostsFragment : Fragment() {

    var postsInteractionListener: PostsFragment.PostsInteractionListener? = null

    private var idTopic: String = ""

    private val postAdapter: PostsAdapter by lazy {
        val adapter = PostsAdapter()
        adapter
    }

    companion object {
        const val ARG_ID = "id"

        fun newInstance(id: String): PostsFragment {
            val fragment = PostsFragment()

            val bundle = Bundle().apply {
                putString(ARG_ID, id)
            }

            fragment.arguments = bundle

            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idTopic = arguments?.getString(ARG_ID).toString()
        return container?.inflate(R.layout.fragment_post)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter.setPosts(PostRepo.posts)

        buttonCreate.setOnClickListener{
            postsInteractionListener?.onCreateTopic(idTopic)
        }

        swipePostRefresh.setOnRefreshListener {
            loadPosts()
            swipePostRefresh.isRefreshing = false
        }

        listPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listPosts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listPosts.adapter = postAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(ARG_ID)?.let {
            idTopic = it
        }

        if (context is PostsFragment.PostsInteractionListener)
            postsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${PostsFragment.PostsInteractionListener::class.java.canonicalName}")
    }

    override fun onResume() {
        super.onResume()
        loadPosts()
    }

    private fun loadPosts(){
    postsInteractionListener?.enableLoading(true)
        context?.let {
            PostRepo.getPosts(
                it.applicationContext,
                idTopic,
                {
                    postsInteractionListener?.enableLoading(false)
                    postAdapter.setPosts(it)
                },
                {
                    postsInteractionListener?.enableLoading(false)
                }
            )
        }
    }

    interface PostsInteractionListener {
        fun enableLoading(enabled: Boolean)
        fun onError()
        fun onCreateTopic(idTopic: String)
    }
}