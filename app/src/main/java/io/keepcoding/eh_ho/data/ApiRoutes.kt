package io.keepcoding.eh_ho.data

import android.net.Uri
import io.keepcoding.eh_ho.BuildConfig

object ApiRoutes {

    fun signIn(username: String) =
        uriBuilder()
            .appendPath("users")
            .appendPath("${username}.json")
            .build()
            .toString()

    fun signUp() =
        uriBuilder()
            .appendPath("users")
            .build()
            .toString()

    fun getTopics() =
        uriBuilder()
            .appendPath("latest.json")
            .build()
            .toString()

    fun createTopic() =
        uriBuilder()
            .appendPath("posts.json")
            .build()
            .toString()

    fun getPosts(id: String) =
        uriBuilder()
            .appendPath("t")
            .appendPath("$id")
            .appendPath("posts.json")
            .build()
            .toString()

    private fun uriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.DiscourseDomain)
}