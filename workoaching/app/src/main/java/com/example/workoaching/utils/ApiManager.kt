package com.example.workoaching.utils

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.controllers.CategoryActivity
import com.example.workoaching.models.*
import com.example.workoaching.recycleradapter.*
import com.shuhart.stepview.StepView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ApiManager {
    companion object{
             var userId : Int? = null
             fun getRecentRoutines(userLoggedInEmail : String, recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getRecentRoutines("recent")

                result.enqueue(object: Callback<ArrayList<Routine>> {
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                       progressBar.visibility = View.GONE
                        val hol = "asdas"
                    }

                    override fun onResponse(call: Call<ArrayList<Routine>>, response: Response<ArrayList<Routine>>) {
                      progressBar.visibility = View.GONE
                        var routines = response.body()
                        recyclerView.apply {
                            layoutManager = pLayoutManager
                            adapter = RecyclerAdapter(routines!!, userLoggedInEmail)
                        }

                    }
                })

            }
        fun getExercisesByCategory(
            recyclerView: RecyclerView,
            pLayoutManager: RecyclerView.LayoutManager,
            progressBar: ProgressBar,
            categoryId: Int,
            stepView: StepView

        ){
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)

            val result: Call<ArrayList<Exercise>> = service.getExerciseByCategory(categoryId)

            result.enqueue(object: Callback<ArrayList<Exercise>> {
                override fun onFailure(call: Call<ArrayList<Exercise>>, t: Throwable) {
                    progressBar.visibility = View.GONE

                }

                override fun onResponse(call: Call<ArrayList<Exercise>>, response: Response<ArrayList<Exercise>>) {
                    progressBar.visibility = View.GONE
                    var exercises = response.body()
                    recyclerView.apply {
                        layoutManager = pLayoutManager
                        adapter = RecyclerAdapterSelectExercises(exercises!!)
                    }
                    stepView.go(categoryId,false)

                }
            })
        }
        fun getAllExercises(sqLiteHelper: SQLiteHelper) {
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Exercise>> = service.getAllExercises()

                result.enqueue(object: Callback<ArrayList<Exercise>> {
                    override fun onFailure(call: Call<ArrayList<Exercise>>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<ArrayList<Exercise>>, response: Response<ArrayList<Exercise>>) {

                        var exercises = response.body()
                        val db=sqLiteHelper.writableDatabase

                        if (exercises != null) {
                            SQLiteHelper.insertExercises(exercises,db)
                        }


                    }
                })
            }

            fun getRecentRoutinesTesting(success:(response : Response<ArrayList<Routine>>) -> Unit, failure:(t:Throwable) -> Unit){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getRecentRoutines("recent")

                result.enqueue(object: Callback<ArrayList<Routine>> {
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(call: Call<ArrayList<Routine>>, response: Response<ArrayList<Routine>>) {
                        success(response)
                    }
                })

            }

            fun getPopularRoutines(userLoggedInEmail : String, recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getRecentRoutines("popular")

                result.enqueue(object: Callback<ArrayList<Routine>> {
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        val hol = "asdas"
                    }

                    override fun onResponse(call: Call<ArrayList<Routine>>, response: Response<ArrayList<Routine>>) {
                        progressBar.visibility = View.GONE
                        var routines = response.body()
                        recyclerView.apply {
                            layoutManager = pLayoutManager
                            adapter = RecyclerAdapter(routines!!, userLoggedInEmail)
                        }

                    }
                })

            }


            fun getPopularRoutinesTesting(success:(response : Response<ArrayList<Routine>>) -> Unit, failure:(t:Throwable) -> Unit){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getRecentRoutines("popular")

                result.enqueue(object: Callback<ArrayList<Routine>> {
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(call: Call<ArrayList<Routine>>, response: Response<ArrayList<Routine>>) {
                        success(response)
                    }
                })

            }

             fun getRoutineById(
                routineId: Int,
                userImage: ImageView,
                routineTitle: TextView,
                recyclerView: RecyclerView,
                pLayoutManager: RecyclerView.LayoutManager,
                progressBar: ProgressBar,
                progressBar2: ProgressBar
            ){
                val service:Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<Routine> = service.getRoutineById(routineId)


                result.enqueue(object: Callback<Routine>{
                    override fun onFailure(call: Call<Routine>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        progressBar2.visibility = View.GONE

                    }

                    override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                        progressBar.visibility = View.GONE
                        progressBar2.visibility = View.GONE


                        var routine = response.body()

                        routineTitle.text = routine!!.Nombre
                        var byteArray:ByteArray? = null
                        byteArray = Base64.getDecoder().decode(routine!!.Imagen)

                        if(byteArray != null && byteArray.isNotEmpty()){

                            userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                        } else {
                            userImage.setImageResource(R.drawable.ic_account_circle_24)
                        }

                        recyclerView.apply {
                            layoutManager = pLayoutManager
                            adapter = RecyclerAdapterExercises(routine!!.Ejercicios)

                        }
                    }
                })

            }

        fun getRoutineExercisesById(
            routineId: Int,
            routineTitle: EditText,
            recyclerView: RecyclerView,
            pLayoutManager: RecyclerView.LayoutManager,
        ){
            val service:Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Routine> = service.getRoutineById(routineId)
            var routine: Routine? = null;

            result.enqueue(object: Callback<Routine>{
                override fun onFailure(call: Call<Routine>, t: Throwable) {



                }

                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {

                     routine = response.body()!!

                    routineTitle.setText( routine!!.Nombre.toString())

                    recyclerView.apply {
                        layoutManager = pLayoutManager
                        adapter = RecyclerAdapterEditExercise(routine?.Ejercicios!!)


                    }
                    RecyclerAdapterEditExercise.setList(routine?.Ejercicios!!)
                }
            })

        }


            fun getAllCategories(recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Category>> = service.getAllCategories()

                result.enqueue(object: Callback<ArrayList<Category>> {
                    override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Category>>,
                        response: Response<ArrayList<Category>>
                    ) {
                        progressBar.visibility = View.GONE

                        var categories = response.body()

                        recyclerView.apply {
                            layoutManager = pLayoutManager
                            adapter = RecyclerAdapterCategories(categories!!)

                        }
                    }

                })

            }

            fun getCategoryRoutines(userLoggedInEmail: String,categoryId: Int,recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getCategoryRoutines(categoryId)

                result.enqueue(object: Callback<ArrayList<Routine>> {
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        progressBar.visibility = View.GONE

                    }

                    override fun onResponse(call: Call<ArrayList<Routine>>, response: Response<ArrayList<Routine>>) {
                        progressBar.visibility = View.GONE
                        CategoryActivity.filterList= response.body()!!
                        recyclerView.apply {
                            layoutManager = pLayoutManager
                            adapter = RecyclerAdapter(CategoryActivity.filterList,userLoggedInEmail)
                        }

                    }
                })
            }

            fun getUserRoutines(userEmail: String,
                               checkOwner : Boolean,
                               recyclerView: RecyclerView,
                               pLayoutManager: RecyclerView.LayoutManager,
                               progressBar: ProgressBar,
                               txtNoRoutines : TextView) {
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getUserRoutines("own", userEmail)

                result.enqueue(object: Callback<ArrayList<Routine>>{
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Routine>>,
                        response: Response<ArrayList<Routine>>
                    ) {
                        progressBar.visibility = View.GONE
                        var routines = response.body()

                        if(routines!!.isNotEmpty()){
                            recyclerView.apply{
                                layoutManager = pLayoutManager
                                adapter = if(checkOwner)
                                    RecyclerAdapter(routines!!, userEmail)
                                else
                                    RecyclerAdapter(routines!!, null)
                            }
                            recyclerView.visibility = View.VISIBLE
                        } else {
                            txtNoRoutines.visibility = View.VISIBLE
                        }


                    }
                })


            }
        fun getRoutineByIdOffline(
            routineId: Int,
            sqLiteHelper: SQLiteHelper
        ){
            val service:Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Routine> = service.getRoutineById(routineId)


            result.enqueue(object: Callback<Routine>{
                override fun onFailure(call: Call<Routine>, t: Throwable) {

                }

                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    var routine = response.body()
                    val db=sqLiteHelper.writableDatabase

                    if (routine != null) {

                        SQLiteHelper.insertRoutineExercise(routine.Ejercicios!!,db,routine.Id_Rutina)
                    }
                }
            })

        }
        fun getUserFavoriteRoutinesOffline(userEmail: String,sqLiteHelper: SQLiteHelper){
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)

            val result: Call<ArrayList<Routine>> = service.getUserRoutines("favorite", userEmail)

            result.enqueue(object: Callback<ArrayList<Routine>>{
                override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<Routine>>,
                    response: Response<ArrayList<Routine>>
                ) {

                    var routines = response.body()
                    val db=sqLiteHelper.writableDatabase
                    if (routines != null) {
                        SQLiteHelper.insertFavoriteRoutines(routines,db,userEmail)

                        for(routine in routines){
                            getRoutineByIdOffline(routine.Id_Rutina!!,sqLiteHelper)
                        }

                    }

                }
            })

        }

            fun getUserFavoriteRoutines(userEmail: String,  recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar, txtNoRoutines : TextView){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Routine>> = service.getUserRoutines("favorite", userEmail)

                result.enqueue(object: Callback<ArrayList<Routine>>{
                    override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Routine>>,
                        response: Response<ArrayList<Routine>>
                    ) {
                        progressBar.visibility = View.GONE
                        var routines = response.body()

                        if(routines!!.isNotEmpty()){
                            recyclerView.apply{
                                layoutManager = pLayoutManager
                                adapter = RecyclerAdapter(routines!!, userEmail)
                            }
                            recyclerView.visibility = View.VISIBLE
                        } else {
                            txtNoRoutines.visibility = View.VISIBLE
                        }
                    }
                })

            }

             fun getUserByEmail(userEmail : String, success: (response: Response<User>) -> Unit, failure: (t: Throwable) -> Unit){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<User> = service.getUserByEmail(userEmail)

                result.enqueue(object: Callback<User>{
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        failure(t)
                    }
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        success(response)
                    }
                })
            }

            fun getGetUserImages(userEmail: String, allowRemove:Boolean, recyclerView: RecyclerView, pLayoutManager: RecyclerView.LayoutManager, progressBar: ProgressBar, txtNoImages : TextView){
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<ArrayList<Image>> = service.getUserImages(userEmail)

                result.enqueue(object: Callback<ArrayList<Image>>{
                    override fun onFailure(call: Call<ArrayList<Image>>, t: Throwable) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Image>>,
                        response: Response<ArrayList<Image>>
                    ) {
                        progressBar.visibility = View.GONE

                        var images = response.body()

                        if(!images.isNullOrEmpty()){
                            recyclerView.apply{
                                layoutManager = pLayoutManager
                                adapter = RecyclerAdapterUserImages(images, allowRemove)
                            }
                            recyclerView.visibility = View.VISIBLE
                        } else {

                            txtNoImages.visibility = View.VISIBLE
                        }
                    }
                })

            }

            fun deleteUserImage(imageId : Int, success: (response: Response<Boolean>) -> Unit, failure: (t: Throwable) -> Unit){
                val data = UserImageProgressData(context="deleteImage", id_image = imageId)
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<Boolean> = service.deleteImage(data)

                result.enqueue(object: Callback<Boolean>{
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        failure(t)
                    }
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        success(response)
                    }
                })

            }

            fun addRoutine(routine : Routine, success: (response: Response<Boolean>) -> Unit, failure: (t: Throwable) -> Unit){
                val service : Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<Boolean> = service.addRoutine(routine)

                result.enqueue(object: Callback<Boolean>{
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        success(response)
                    }
                })

            }
            fun addOfflineRoutines(routines: List<Routine>, success: (response: Response<Boolean>) -> Unit, failure: (t: Throwable) -> Unit){
                val service : Service = RestEngine.getRestEngine().create(Service::class.java)

                for(routine in routines){

                val result: Call<Boolean> = service.addRoutine(routine)

                result.enqueue(object: Callback<Boolean>{
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        success(response)
                    }
                })

                }
            }
            fun updateRoutine(routine: Routine, success: (response: Response<Boolean>) -> Unit, failure: (t: Throwable) -> Unit){
                val routineData = RoutineUpdateData(context = "editRoutine", Rutina = routine)


                val service : Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<Boolean> = service.updateRoutine(routineData)

                result.enqueue(object: Callback<Boolean>{
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        success(response)
                    }
                })

            }


    }


}