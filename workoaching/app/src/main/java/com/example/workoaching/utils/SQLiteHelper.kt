package com.example.workoaching.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.models.Exercise
import com.example.workoaching.models.Routine

import com.example.workoaching.models.User
import com.example.workoaching.recycleradapter.RecyclerAdapter
import com.example.workoaching.recycleradapter.RecyclerAdapterExercises
import com.example.workoaching.recycleradapter.RecyclerAdapterSelectExercises
import com.shuhart.stepview.StepView
import java.lang.Exception
import java.util.*

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    lateinit var shared : SharedPreferences
    companion object{
        lateinit var  context  : Context
        private const val DATABASE_NAME="workoaching_db"
        private const val DATABASE_VERSION=1



        fun insertExercises(exercises: List<Exercise>, db: SQLiteDatabase): Long? {

            var success : Long? =-1
            // contentValues.put( DatabaseDAO.UserInfo.COL_ID,user.id_usuario)
            for (exercise in exercises){

                val contentValues=ContentValues()
                contentValues.put( DatabaseInfo.ExerciseInfo.COL_NOMBRE,exercise.Nombre)
                contentValues.put( DatabaseInfo.ExerciseInfo.COL_IMAGEN,exercise.Imagen)
                contentValues.put( DatabaseInfo.ExerciseInfo.COL_ID,exercise.Id_ejercicio)
                contentValues.put( DatabaseInfo.ExerciseInfo.COL_IDCATEGORIA,exercise.Id_Categoria)

                success=db.insert( DatabaseInfo.ExerciseInfo.TABLE_NAME,null,contentValues)

            }
            db.close()
            return success
        }
        fun insertRoutineExercise(exercises: List<Exercise>, db: SQLiteDatabase, idRutina: Int?): Long? {
            var success : Long? =-1
            // contentValues.put( DatabaseDAO.UserInfo.COL_ID,user.id_usuario)
            for (exercise in exercises){

                val contentValues=ContentValues()
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_NOMBRE,exercise.Nombre)
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA,idRutina)
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_IDEJERCICIO,exercise.Id_ejercicio)
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_IMAGEN,exercise.Imagen)
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_REPETICIONES,exercise.Repeticiones)
                contentValues.put( DatabaseInfo.RoutinesExercisesInfo.COL_SERIES,exercise.Series)

                success=db.insert( DatabaseInfo.RoutinesExercisesInfo.TABLE_NAME,null,contentValues)
            }
            db.close()
            return success
        }
        fun insertFavoriteRoutines(routines: List<Routine>, db: SQLiteDatabase, userEmail: String): Long? {

            var success : Long? =-1
            // contentValues.put( DatabaseDAO.UserInfo.COL_ID,user.id_usuario)
            for (routine in routines){

                val contentValues=ContentValues()
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE,routine.Nombre)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA,routine.Id_Rutina)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO,routine.Id_Usuario)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL,routine.Email)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_IMAGEN,routine.Imagen)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS,routine.Categorias)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR,routine.Autor)
                contentValues.put( DatabaseInfo.FavoriteRoutinesInfo.COL_USERLOGGED,userEmail)

                success=db.insert( DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME,null,contentValues)


            }
            db.close()
            return success
        }


    }





    override fun onCreate(db: SQLiteDatabase?) {
        try{
            val createTableUser=("CREATE TABLE "+  DatabaseInfo.UserInfo.TABLE_NAME + "("
                    + DatabaseInfo.UserInfo.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +  DatabaseInfo.UserInfo.COL_NOMBRE + " TEXT,"
                    +  DatabaseInfo.UserInfo.COL_APELLIDOP + " TEXT,"
                    +  DatabaseInfo.UserInfo.COL_APELLIDOM + " TEXT,"
                    +  DatabaseInfo.UserInfo.COL_TELEFONO + " TEXT,"
                    +  DatabaseInfo.UserInfo.COL_CONTRASENA + " TEXT,"
                    +  DatabaseInfo.UserInfo.COL_IMAGEN + " BLOB,"
                    +  DatabaseInfo.UserInfo.COL_EMAIL +" TEXT)"
                    )
            db?.execSQL(createTableUser)

            val createTableEjercicio=("CREATE TABLE "+  DatabaseInfo.ExerciseInfo.TABLE_NAME + "("
                    + DatabaseInfo.ExerciseInfo.COL_ID + " INTEGER PRIMARY KEY, "
                    +  DatabaseInfo.ExerciseInfo.COL_NOMBRE + " TEXT,"
                    +  DatabaseInfo.ExerciseInfo.COL_IMAGEN+ " BLOB,"
                    +  DatabaseInfo.ExerciseInfo.COL_IDCATEGORIA + " INTEGER)"
                    )
            db?.execSQL(createTableEjercicio)


            val favoriteExercisesTable=("CREATE TABLE "+  DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME + "("
                    + DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA+ " INTEGER, "
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE + " TEXT,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR + " TEXT,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL + " TEXT,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_IMAGEN + " BLOB,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO+ " INTEGER,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_USERLOGGED+ " INTEGER,"
                    +  DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS + " TEXT,"+
                    "PRIMARY KEY("+ DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA+","+DatabaseInfo.FavoriteRoutinesInfo.COL_USERLOGGED+"))"
                    )
            db?.execSQL(favoriteExercisesTable)

            val routineExercisesTable=("CREATE TABLE "+  DatabaseInfo.RoutinesExercisesInfo.TABLE_NAME + "("
                    + DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA+ " INTEGER NOT NULL, "
                    +  DatabaseInfo.RoutinesExercisesInfo.COL_NOMBRE + " TEXT,"
                    +  DatabaseInfo.RoutinesExercisesInfo.COL_IDEJERCICIO + " INTEGER NOT NULL,"
                    +  DatabaseInfo.RoutinesExercisesInfo.COL_IMAGEN + " BLOB,"
                    +  DatabaseInfo.RoutinesExercisesInfo.COL_REPETICIONES+ " INTEGER,"
                    +  DatabaseInfo.RoutinesExercisesInfo.COL_SERIES+ " INTEGER," +
                    "PRIMARY KEY("+ DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA+","+DatabaseInfo.RoutinesExercisesInfo.COL_IDEJERCICIO+"))"

                    )
            db?.execSQL(routineExercisesTable)


            shared = context.getSharedPreferences("login", Context.MODE_PRIVATE)

            val userEmail = shared.getString("userEmail", "error")?:"null"
            ApiManager.getAllExercises(this)

            ApiManager.getUserFavoriteRoutinesOffline(userEmail,this)
        }catch (e : Exception){
            e.printStackTrace()
        }


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        db?.execSQL("DROP TABLE IF EXISTS ${ DatabaseInfo.UserInfo.TABLE_NAME} ")
        db?.execSQL("DROP TABLE IF EXISTS ${ DatabaseInfo.RoutinesExercisesInfo.TABLE_NAME} ")
        db?.execSQL("DROP TABLE IF EXISTS ${ DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME} ")
        db?.execSQL("DROP TABLE IF EXISTS ${ DatabaseInfo.ExerciseInfo.TABLE_NAME} ")
        onCreate(db)
    }

    fun insertUser(user  :User): Long {
        val db=this.writableDatabase
        val contentValues=ContentValues()
       // contentValues.put( DatabaseDAO.UserInfo.COL_ID,user.id_usuario)
        contentValues.put( DatabaseInfo.UserInfo.COL_NOMBRE,user.nombre)
        contentValues.put( DatabaseInfo.UserInfo.COL_APELLIDOP,user.apellido_p)
        contentValues.put( DatabaseInfo.UserInfo.COL_APELLIDOM,user.apellido_m)
       contentValues.put( DatabaseInfo.UserInfo.COL_IMAGEN,user.imagen)
        contentValues.put(DatabaseInfo.UserInfo.COL_CONTRASENA,user.contrasena)
        contentValues.put( DatabaseInfo.UserInfo.COL_TELEFONO,user.telefono)
       // contentValues.put( DatabaseDAO.UserInfo.COL_DIRECCION,user.telefono)
        contentValues.put( DatabaseInfo.UserInfo.COL_EMAIL,user.email)

        val success=db.insert( DatabaseInfo.UserInfo.TABLE_NAME,null,contentValues)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun getExercisesByCategory(
        recyclerView: RecyclerView,
        pLayoutManager: RecyclerView.LayoutManager,
        progressBar: ProgressBar,
        categoryId: Int,
        stepView: StepView

    ){
        val columns:Array<String> =  arrayOf( DatabaseInfo.ExerciseInfo.COL_NOMBRE ,
            DatabaseInfo.ExerciseInfo.COL_IMAGEN,DatabaseInfo.ExerciseInfo.COL_IDCATEGORIA , DatabaseInfo.ExerciseInfo.COL_ID)

        val db=this.readableDatabase

        val exerciseList= mutableListOf<Exercise>()
        val cursor: Cursor?
        val whereClause = "${ DatabaseInfo.ExerciseInfo.COL_IDCATEGORIA}"+"="+"${categoryId}"

        try {
            cursor =  db.query(
                DatabaseInfo.ExerciseInfo.TABLE_NAME,
                columns,
                whereClause,
                null,
                null,
                null,
                null)


            if(cursor.moveToFirst()){

                do{
                    var exercise= Exercise ();

                //user.id_usuario=cursor.getInt(cursor.getColumnIndex(DatabaseDAO.UserInfo.COL_ID)).toInt()
                exercise.Nombre=cursor.getString(cursor.getColumnIndex( DatabaseInfo.ExerciseInfo.COL_NOMBRE)).toString()
                exercise.Imagen=cursor.getString(cursor.getColumnIndex( DatabaseInfo.ExerciseInfo.COL_IMAGEN)).toString()
                exercise.Id_ejercicio=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.ExerciseInfo.COL_ID))
                // user.contrasena=cursor.getString(cursor.getColumnIndex( DatabaseDAO.UserInfo.COL_CONTRASENA)).toString()
                exercise.Id_Categoria=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.ExerciseInfo.COL_IDCATEGORIA))


                    exerciseList.add(exercise)

                }while(cursor.moveToNext())
            }
            db.close()


        }catch(e: Exception){
            e.printStackTrace()
            db.close()
        }

        recyclerView.apply {
            layoutManager = pLayoutManager
            adapter = RecyclerAdapterSelectExercises(exerciseList!!)
        }
        stepView.go(categoryId,false)

        progressBar.visibility=View.GONE


    }

    @SuppressLint("Range")
    fun getRoutineById(
        routineId: Int,
        userImage: ImageView,
        routineTitle: TextView,
        recyclerView: RecyclerView,
        pLayoutManager: RecyclerView.LayoutManager,
        progressBar: ProgressBar,
        progressBar2: ProgressBar
    ){
        //Obtengo la rutina por su ID
        val db=this.readableDatabase

        val columnsRoutine:Array<String> =  arrayOf( DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE ,
            DatabaseInfo.FavoriteRoutinesInfo.COL_IMAGEN,DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO,DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA,
            DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR, DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL,DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS)


        val routine= Routine()
        val cursorRoutine: Cursor?
        val whereRoutineClause = "${ DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA}"+"="+"${routineId}"

        try {
            cursorRoutine =  db.query(
                DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME,
                columnsRoutine,
                whereRoutineClause,
                null,
                null,
                null,
                null)




            if(cursorRoutine.moveToFirst()){
                routine.Nombre=cursorRoutine.getString(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE)).toString()
                routine.Email=cursorRoutine.getString(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL)).toString()
                routine.Imagen=cursorRoutine.getString(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_IMAGEN)).toString()

                routine.Id_Usuario=cursorRoutine.getInt(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO))
                routine.Id_Rutina=cursorRoutine.getInt(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA))

                routine.Autor=cursorRoutine.getString(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR)).toString()
                routine.Categorias=cursorRoutine.getString(cursorRoutine.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS)).toString()

            }


        }catch(e: Exception){
            e.printStackTrace()
        }


        //Obtengo los ejercicios de ESA rutina

        val columns:Array<String> =  arrayOf( DatabaseInfo.RoutinesExercisesInfo.COL_NOMBRE ,
            DatabaseInfo.RoutinesExercisesInfo.COL_IMAGEN,DatabaseInfo.RoutinesExercisesInfo.COL_IDEJERCICIO,DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA,
            DatabaseInfo.RoutinesExercisesInfo.COL_REPETICIONES, DatabaseInfo.RoutinesExercisesInfo.COL_SERIES)


        val exerciseList= mutableListOf<Exercise>()
        val cursor: Cursor?
        val whereClause = "${ DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA}"+"="+"${routineId}"

        try {
            cursor =  db.query(
                DatabaseInfo.RoutinesExercisesInfo.TABLE_NAME,
                columns,
                whereClause,
                null,
                null,
                null,
                null)


            if(cursor.moveToFirst()){

                do{
                    var exercise= Exercise ();

                   exercise.Series=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_SERIES))
                    exercise.Repeticiones=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_REPETICIONES))
                    exercise.Nombre=cursor.getString(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_NOMBRE)).toString()
                    exercise.Imagen=cursor.getString(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_IMAGEN)).toString()
                    exercise.Id_ejercicio=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_IDEJERCICIO))
                    exercise.Id_Rutina=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.RoutinesExercisesInfo.COL_IDRUTINA))

                    exerciseList.add(exercise)

                }while(cursor.moveToNext())
            }

            routine.Ejercicios=exerciseList


            routineTitle.text = routine!!.Nombre
            var byteArray:ByteArray? = null
            byteArray = Base64.getDecoder().decode(routine!!.Imagen)

            if(byteArray != null){

                userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }

            recyclerView.apply {
                layoutManager = pLayoutManager
                adapter = RecyclerAdapterExercises(routine!!.Ejercicios)

            }

            db.close()


        }catch(e: Exception){
            e.printStackTrace()
            db.close()
        }

        progressBar.visibility = View.GONE
        progressBar2.visibility = View.GONE

    }

   fun deleteFavoriteRoutineByIdAndEmail(idRoutine:Int,email: String,):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA} = ? and " +
                    "${DatabaseInfo.FavoriteRoutinesInfo.COL_USERLOGGED} = ? ;"

            val db=this.writableDatabase


            val _success = db.delete(
                DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME,
                where,
                arrayOf(idRoutine.toString(),email))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }

        return  boolResult
    }

    @SuppressLint("Range")
    fun getFavoriteRoutines(
        recyclerView: RecyclerView,
        pLayoutManager: RecyclerView.LayoutManager,
        progressBar: ProgressBar,
        userEmail: String
    ){
        val columns:Array<String> =  arrayOf( DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR ,
            DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS,DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL , DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA
            , DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO,DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE)

        val db=this.readableDatabase

        val RoutineList= mutableListOf<Routine>()
        val cursor: Cursor?
        val whereClause = "${ DatabaseInfo.FavoriteRoutinesInfo.COL_USERLOGGED}"+"="+"'${userEmail}'"
        try {
            cursor =  db.query(
                DatabaseInfo.FavoriteRoutinesInfo.TABLE_NAME,
                columns,
                whereClause,
                null,
                null,
                null,
                null)


            if(cursor.moveToFirst()){
                do{
                    var routine= Routine ();

                    routine.Nombre=cursor.getString(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_NOMBRE)).toString()
                    routine.Autor=cursor.getString(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_AUTOR)).toString()
                    routine.Email =cursor.getString(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_EMAIL)).toString()

                    routine.Categorias=cursor.getString(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_CATEGORIAS)).toString()
                    routine.Id_Rutina=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_IDRUTINA))

                    routine.Id_Usuario=cursor.getInt(cursor.getColumnIndex( DatabaseInfo.FavoriteRoutinesInfo.COL_IDUSUARIO))
                    RoutineList.add(routine)


                }while(cursor.moveToNext())
            }
            db.close()

            recyclerView.apply {
                layoutManager = pLayoutManager
                adapter = RecyclerAdapter(RoutineList!!,"")
            }

            recyclerView.visibility = View.VISIBLE
        }catch(e: Exception){
            e.printStackTrace()
            db.close()
        }



        progressBar.visibility=View.GONE


    }

    @SuppressLint("Range")
    fun getUser(email : String) : User? {
        var user = User ();
        val columns:Array<String> =  arrayOf( DatabaseInfo.UserInfo.COL_NOMBRE ,  DatabaseInfo.UserInfo.COL_APELLIDOP,DatabaseInfo.UserInfo.COL_APELLIDOM , DatabaseInfo.UserInfo.COL_IMAGEN,
        DatabaseInfo.UserInfo.COL_CONTRASENA , DatabaseInfo.UserInfo.COL_TELEFONO, DatabaseInfo.UserInfo.COL_EMAIL)


        val db=this.readableDatabase


        val cursor: Cursor?
        val whereClause = "${ DatabaseInfo.UserInfo.COL_EMAIL}"+"="+"'${email}'"

        try {
             cursor =  db.query(
                DatabaseInfo.UserInfo.TABLE_NAME,
                columns,
                whereClause,
                null,
                null,
                null,
                null)




        if(cursor.moveToFirst()){
            //user.id_usuario=cursor.getInt(cursor.getColumnIndex(DatabaseDAO.UserInfo.COL_ID)).toInt()
            user.nombre=cursor.getString(cursor.getColumnIndex( DatabaseInfo.UserInfo.COL_NOMBRE)).toString()
            user.email=cursor.getString(cursor.getColumnIndex( DatabaseInfo.UserInfo.COL_EMAIL)).toString()
            user.apellido_m=cursor.getString(cursor.getColumnIndex( DatabaseInfo.UserInfo.COL_APELLIDOM)).toString()
           // user.contrasena=cursor.getString(cursor.getColumnIndex( DatabaseDAO.UserInfo.COL_CONTRASENA)).toString()
            user.apellido_p=cursor.getString(cursor.getColumnIndex( DatabaseInfo.UserInfo.COL_APELLIDOP)).toString()
            user.imagen=cursor.getString(cursor.getColumnIndex( DatabaseInfo.UserInfo.COL_IMAGEN)).toString()
           // user.contrasena=cursor.getString(cursor.getColumnIndex( DatabaseDAO.UserInfo.COL_CONTRASENA)).toString()
            //user.telefono=cursor.getString(cursor.getColumnIndex( DatabaseDAO.UserInfo.COL_TELEFONO)).toString()
        }
        db.close()
        return user

        }catch(e: Exception){

            e.printStackTrace()
            db.close()
            return null
        }
    }
}