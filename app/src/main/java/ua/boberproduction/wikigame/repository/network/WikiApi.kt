package ua.boberproduction.wikigame.repository.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    companion object {
        const val BASE_URL = "https://en.wikipedia.org/w/"
    }

    @GET("api.php?action=parse&format=json&summary=&redirects=1&prop=text")
    fun getArticleHtml(@Query("page") articleName: String): Single<ArticleResponse>
}