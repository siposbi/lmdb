package hu.bme.aut.android.kliensalk_hf_2_android.network

import hu.bme.aut.android.kliensalk_hf_2_android.network.model.OMDBData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface OMDBApi {

    @GET("/")
    fun getMovieData(
        @Query("t") title: String?,
        @Query("y") year: String?,
        @Query("apikey") apikey: String?
    ): Call<OMDBData?>?
}
