package io.keepcoding.eh_ho.posts

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.LoadingDialogFragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.CreatePostModel
import io.keepcoding.eh_ho.data.PostRepo
import io.keepcoding.eh_ho.data.RequestError
import io.keepcoding.eh_ho.data.TopicsRepo
import io.keepcoding.eh_ho.inflate
import io.keepcoding.eh_ho.topics.CreateTopicFragment
import io.keepcoding.eh_ho.topics.TAG_LOADING_DIALOG
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.android.synthetic.main.fragment_create_post.container
import kotlinx.android.synthetic.main.fragment_create_post.inputContent
import kotlinx.android.synthetic.main.fragment_create_topic.*
import java.lang.IllegalArgumentException

class CreatePostFragment : Fragment() {

    var interactionListener: CreatePostInteractionListener? = null
    val loadingDialogFragment: LoadingDialogFragment by lazy {
        val message = "Creating Post"
        LoadingDialogFragment.newInstance(message)
    }

    companion object {

        private var idTopic: String = ""

        fun newInstance(idTopic: String) : CreatePostFragment {
            var fragment = CreatePostFragment()

            val bundle = Bundle().apply {
                putString(ARG_ID_TOPIC, idTopic)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CreatePostInteractionListener)
            this.interactionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${CreatePostFragment.CreatePostInteractionListener::class.java.canonicalName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idTopic = arguments?.getString(ARG_ID_TOPIC).toString()
        return container?.inflate(R.layout.fragment_create_post)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topic = TopicsRepo.getTopic(idTopic)
        topic?.let {
            textTitleTopic.text = topic.title.toString()
            textIdTopic.text = topic.id.toString()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_post, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_post_send -> sendCreatePost()
        }
        return true
    }

    private fun sendCreatePost(){
        if (isFormValid()){
            postPosts()
        }
        else{
            showErrors()
        }
    }

    private fun postPosts(){
        enableLoadingDialog()
        val model = CreatePostModel(
            idTopic,
            inputContent.text.toString()
        )

        context?.let {
            PostRepo.addPost(
                it.applicationContext,
                model,
                {
                    enableLoadingDialog(false)
                    interactionListener?.onPostCreated(idTopic)
                },
                {
                    enableLoadingDialog(false)
                    handleError(it)
                }
            )
        }
    }

    private fun enableLoadingDialog(enabled: Boolean = true) {
        if (enabled)
            loadingDialogFragment.show(childFragmentManager, TAG_LOADING_DIALOG)
        else
            loadingDialogFragment.dismiss()
    }

    private fun handleError(error: RequestError) {
        val message =
            if (error.messageResId != null)
                getString(error.messageResId)
            else error.message ?: getString(R.string.error_default)

        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showErrors(){
        if (inputContent.text.isEmpty()){
            inputContent.error = "This field cannot be empty"
        } else if (inputContent.text.length< 20) {
            inputContent.error = "This field must have minimum 20 characters"
        }



    }

    private fun isFormValid() = inputContent.text.isNotEmpty() && inputContent.text.length>=20

    interface CreatePostInteractionListener {
        fun onPostCreated(idTopic: String)
    }
}