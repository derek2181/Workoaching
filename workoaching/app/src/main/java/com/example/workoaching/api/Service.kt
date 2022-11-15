package com.example.workoaching.api

import com.example.workoaching.models.*
import retrofit2.Call
import retrofit2.http.*

interface Service {
//    @GET("")
//    fun getUserByEmail():Call<List<User>>

    @GET("index.php")
    fun getAllRoutines():Call<ArrayList<Routine>>

    @GET("index.php")
    fun getRecentRoutines(@Query("context") context:String):Call<ArrayList<Routine>>

    @GET("index.php")
    fun getCategoryRoutines(@Query("categoryId") categoryId:Int):Call<ArrayList<Routine>>


    @GET("index.php")
    fun getRoutineById(@Query("id") id:Int):Call<Routine>

    @GET("categories.php")
    fun getAllCategories():Call<ArrayList<Category>>

    @GET("exercises.php")
    fun getExerciseByCategory(@Query("id") id:Int):Call<ArrayList<Exercise>>


    @GET("exercises.php")
    fun getAllExercises():Call<ArrayList<Exercise>>

//    @FormUrlEncoded
//    @POST("users.php")
//    fun login(@Field("email") email :String, @Field("password") password:String, @Field("context") context:String):Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("users.php")
    fun login(@Body loginData: LoginData):Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("users.php")
    fun register(@Body registerData: User):Call<Int>

    @GET("users.php")
    fun getUserByEmail(@Query("email") email :String):Call<User>

    @Headers("Content-Type: application/json")
    @POST("users.php")
    fun changeUserImage(@Body userImageData: UserImageData):Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("images.php")
    fun addImage(@Body imageData: UserImageProgressData):Call<Int>

    @Headers("Content-Type: application/json")
    @POST("images.php")
    fun deleteImage(@Body imageData: UserImageProgressData):Call<Boolean>


    @Headers("Content-Type: application/json")
    @POST("users.php")
    fun changeUserPassword(@Body passwordData: ChangePasswordData):Call<Boolean>

    @GET("images.php")
    fun getUserImages(@Query("email") email:String):Call<ArrayList<Image>>

    @GET("index.php")
    fun getUserRoutines(@Query("context") context: String,@Query("userEmail") email:String):Call<ArrayList<Routine>>

    @Headers("Content-Type: application/json")
    @POST("index.php")
    fun deleteRoutineById(@Body routineData: RoutineData):Call<Boolean>

    @GET("index.php")
    fun isRoutineAddedByUser(@Query("id") id: Int, @Query("email") email: String):Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("index.php")
    fun toggleFavoriteRoutine(@Body favoriteRoutineData: FavoriteRoutineData):Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("index.php")
    fun addRoutine(@Body routine:Routine):Call<Boolean>


    @Headers("Content-Type: application/json")
    @POST("index.php")
    fun updateRoutine(@Body routineData:RoutineUpdateData):Call<Boolean>

}