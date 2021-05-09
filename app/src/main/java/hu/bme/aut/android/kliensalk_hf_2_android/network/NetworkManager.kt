package hu.bme.aut.android.kliensalk_hf_2_android.network

import hu.bme.aut.android.kliensalk_hf_2_android.network.model.OMDBData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val omdbApi: OMDBApi

    private const val SERVICE_URL = "http://www.omdbapi.com/"
    private const val APP_ID = "ab1a7e32"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        omdbApi = retrofit.create(OMDBApi::class.java)
    }

    fun getMovie(title: String?, year: String?): Call<OMDBData?>? {
        return omdbApi.getMovieData(title, year, APP_ID)
    }
}