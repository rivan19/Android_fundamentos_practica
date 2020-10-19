package io.keepcoding.eh_ho.data

import java.text.SimpleDateFormat
import org.json.JSONObject
import java.util.*

data class Post (
    val id: String = "",
    val username: String = "",
    val cooked: String = "",
    val created_at: Date = Date()
) {
    companion object {
        fun parsePostsList(response: JSONObject): List<Post> {
            val objectList = response.getJSONObject("post_stream")
                .getJSONArray("posts")

            val posts = mutableListOf<Post>()

            for (i in 0 until objectList.length()){
                val parsedTopic = parseTopic(objectList.getJSONObject(i))
                posts.add(parsedTopic)
            }

            return posts
        }

        private fun parseTopic(jsonObject: JSONObject) : Post {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()

            return Post(
                id = jsonObject.getInt("id").toString(),
                username = jsonObject.getString("username").toString(),
                created_at = dateFormatted,
                cooked = jsonObject.getString("cooked").toString()
            )
        }
    }

}