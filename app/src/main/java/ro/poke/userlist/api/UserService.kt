package ro.poke.userlist.api

import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ro.poke.userlist.data.entity.UserListResponse

/**
 * Used to connect to the Unsplash API to fetch photos
 */
interface UserService {

    @GET("api")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") results: Int,
        @Query("seed") seed: String,
    ): UserListResponse

    companion object {
        private const val BASE_URL = "https://randomuser.me/"

        fun create(): UserService {
            val client = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserService::class.java)
        }

        val userService :  UserService by lazy{
            UserService.create()
        }
    }
}
