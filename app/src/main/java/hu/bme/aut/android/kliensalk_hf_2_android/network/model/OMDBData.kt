package hu.bme.aut.android.kliensalk_hf_2_android.network.model

import com.google.gson.annotations.SerializedName

class OMDBData {
    @SerializedName("Title")
    var title: String? = null
    @SerializedName("Year")
    var year: String? = null
    @SerializedName("Genre")
    var genre: String? = null
    @SerializedName("Plot")
    var plot: String? = null
    @SerializedName("Poster")
    var poster: String? = null
    @SerializedName("Response")
    var hasResponse: String? = null
    @SerializedName("Error")
    var error: String? = null
}