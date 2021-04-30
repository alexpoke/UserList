package ro.poke.userlist.data

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @field:SerializedName("results") val results: List<User>
)
