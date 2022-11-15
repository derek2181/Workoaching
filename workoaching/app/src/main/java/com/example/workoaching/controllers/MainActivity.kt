package com.example.workoaching.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.Routine
import com.example.workoaching.models.User
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.ImageUtilities
import com.example.workoaching.utils.SQLiteHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import android.database.CursorWindow
import android.net.Network
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.workoaching.DAO.DatabaseInfo.Companion.db
import com.example.workoaching.utils.NetworkConnection
import java.lang.Exception
import java.lang.reflect.Field


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myToolbar: Toolbar
    lateinit var shared : SharedPreferences
    private var routines:List<Routine>? = null
    private lateinit var networkConnection: NetworkConnection
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar!!.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
//
//        supportActionBar!!.customView = action_bar
        //throw RuntimeException("Test Crash")
        supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)

        setContentView(R.layout.activity_main)
        SQLiteHelper.context=this
        val userImageBtn=findViewById<ImageView>(R.id.user_btn)
        DatabaseInfo.setContext(this)

        shared = getSharedPreferences("login", Context.MODE_PRIVATE)

        networkConnection= NetworkConnection(applicationContext)

        val userEmail = shared.getString("userEmail", "error")?:"null"
        networkConnection.observe(this,Observer{isConnected->

            if(isConnected){
                ApiManager.getUserByEmail(userEmail, {response ->
                    val user = response.body()
                    var byteArray:ByteArray? = null
                    if(!user!!.imagen.isNullOrEmpty()){
                        byteArray = Base64.getDecoder().decode(user!!.imagen)
                        if(byteArray != null){
                            userImageBtn.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

                        }
                    }

                    val edit = shared.edit()
                    edit.putInt("id_usuario", user.id_usuario?:0);
                    edit.apply()


                    //TODO crear las tablas que faltan, hacer que se puedan agregar las rutinas de forma local y validar lo de la conexion a internet

                    val result= DatabaseInfo.db.getUser(user.email.toString())
                    if(result==null){
                        DatabaseInfo.db.insertUser(user)
                    }


                }, {t ->
                    Toast.makeText(this, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
                })



                if(Routine.offlineRoutinesCreated.size>0){
                    ApiManager.addOfflineRoutines(Routine.offlineRoutinesCreated,{success->

                        Toast.makeText(this,R.string.offline_routines_added,Toast.LENGTH_LONG).show()
                        Routine.offlineRoutinesCreated.clear()


                    },{failure->


                    })
                }
                try {
                    ApiManager.getAllExercises(SQLiteHelper(this))

                    ApiManager.getUserFavoriteRoutinesOffline(userEmail,SQLiteHelper(this))
                }catch (e  :Exception){
                    e.printStackTrace()
                }
            }else{
                val userEmail = shared.getString("userEmail", "error")?:"null"

            }
        })



//        getUserByEmail(userEmail)


        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // LA ALTURA DE LA VENTANA PARA LOS INPUTS
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);




        user_btn.setOnClickListener(this)
        search_btn.setOnClickListener(this)

//        ApiManager.getRecentRoutinesTesting(::SuccessGetRecentRoutines, ::FailureGetRecentRoutines)

//        ApiManager.getRecentRoutinesTesting({ response   ->
//            val routines = response.body()
//
//            Toast.makeText(this, "Tuve exito ${routines!![0]}", Toast.LENGTH_LONG).show()
//
//        }, { t  ->
//            Toast.makeText(this, "Falle", Toast.LENGTH_LONG).show()
//        })

        ApiManager.getUserByEmail(userEmail, {response ->
            val user = response.body()
            var byteArray:ByteArray? = null
            if(!user!!.imagen.isNullOrEmpty()){
                byteArray = Base64.getDecoder().decode(user!!.imagen)
                if(byteArray != null){
                    userImageBtn.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray!!))

                }
            }

            val edit = shared.edit()
            edit.putInt("id_usuario", user.id_usuario?:0);
            edit.apply()




           val result= DatabaseInfo.db.getUser(user.email.toString())
            if(result==null){
                DatabaseInfo.db.insertUser(user)
            }
            DatabaseInfo.db

      }, {t ->
            Toast.makeText(this, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
        })

    }

    private fun SuccessGetRecentRoutines(response: Response<ArrayList<Routine>>){
        val routines = response.body()
        Toast.makeText(this, "Tuve exito ${routines!![0]}", Toast.LENGTH_LONG).show()
    }

    private fun FailureGetRecentRoutines(t : Throwable){
        Toast.makeText(this, "Falle", Toast.LENGTH_LONG).show()
    }


    override fun onStart(){
        super.onStart()
        setupBottomNavigationMenu()
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.search_btn -> {
//                Toast.makeText(this, "Has pulsado el boton de busqueda", Toast.LENGTH_SHORT).show()
//                replaceFragment(SearchFragment())


                if(networkConnection.isOnline()){
                    val searchIntent = Intent(this, SearchActivity::class.java)

                    startActivity(searchIntent)
                }else{
                    Toast.makeText(this,R.string.check_internet_conection,Toast.LENGTH_LONG).show()
                }


            }
            R.id.user_btn -> {
                if(networkConnection.isOnline()) {
                    val userIntent = Intent(this, UserActivity::class.java)

                    startActivity(userIntent)
                }else{
                    Toast.makeText(this,R.string.check_internet_conection,Toast.LENGTH_LONG).show()
                }


//                Toast.makeText(this, "Has pulsado el boton de usuario", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupBottomNavigationMenu() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun getUserByEmail(userEmail : String){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<User> = service.getUserByEmail(userEmail)

        result.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                var byteArray:ByteArray? = null
                if(!user!!.imagen.isNullOrEmpty()){
                    byteArray = Base64.getDecoder().decode(user!!.imagen)
                    if(byteArray != null){
                        user_btn.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

                    }
                }

                val edit = shared.edit()
                ApiManager.userId = user.id_usuario
                edit.putInt("id_usuario", user.id_usuario?:0);
                edit.apply()

            }
        })
    }

}