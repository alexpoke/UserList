package ro.poke.userlist.data.entity

import com.google.gson.annotations.SerializedName

class Info (
    @SerializedName("page") val page : Int,
    @SerializedName("results") val resullts : Int,
    @SerializedName("seed") val seed : String,
)

