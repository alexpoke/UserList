package ro.poke.userlist.data.entity

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @field:SerializedName("results") val results: List<User>,
    @field:SerializedName("info") val info: Info
)
