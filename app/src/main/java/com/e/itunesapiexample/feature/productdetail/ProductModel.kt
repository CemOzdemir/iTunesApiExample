package com.e.itunesapiexample.feature.productdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(
        @SerializedName("wrapperType")val wrapperType: WrapperType?,
        @SerializedName("collectionName")val collectionName: String?,
        @SerializedName("trackName")val trackName: String?,
        @SerializedName("collectionPrice")val collectionPrice: String?,
        @SerializedName("currency")val currency: String?,
        @SerializedName("releaseDate")val releaseDate: String?,
        @SerializedName("shortDescription")val shortDescription: String?,
        @SerializedName("description")val description: String?,
        @SerializedName("primaryGenreName")val primaryGenreName: String?,
        @SerializedName("artworkUrl100")val artworkUrl100: String?
): Parcelable {
        enum class WrapperType {
                @SerializedName("movie")
                MOVIE,
                @SerializedName("music")
                MUSIC,
                @SerializedName("ebook")
                EBOOK,
                @SerializedName("software")
                SOFTWARE
        }
}