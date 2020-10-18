package io.keepcoding.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.login.SignInFragment
import kotlinx.android.synthetic.main.fragment_topics_error.*

class TopicsFragmentErrorLoading : Fragment() {
    var topicsFragmentErrorLoadingListener: TopicsFragmentErrorLoadingListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topics_error, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TopicsFragmentErrorLoadingListener)
        {
            topicsFragmentErrorLoadingListener = context
        }
        else
            throw IllegalArgumentException("Context doesn't implement ${TopicsFragmentErrorLoading.TopicsFragmentErrorLoadingListener::class.java.canonicalName}")
    }

    override fun onDetach() {
        topicsFragmentErrorLoadingListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRetry.setOnClickListener{
            topicsFragmentErrorLoadingListener?.retry()
        }
    }

    interface TopicsFragmentErrorLoadingListener {
        fun retry()
    }
}