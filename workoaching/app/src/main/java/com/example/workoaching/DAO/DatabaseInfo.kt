package com.example.workoaching.DAO

import android.content.Context
import com.example.workoaching.utils.SQLiteHelper

class DatabaseInfo() {
    companion object{

    public  lateinit var db : SQLiteHelper
    fun setContext(context  :Context){
        db=SQLiteHelper(context)
    }

    }

    abstract  class UserInfo{

        companion object{
            val TABLE_NAME="usuarios"
            val COL_IMAGEN="Imagen"
            val COL_ID="Id_Usuario"
            val COL_DIRECCION="Direccion"
            val COL_TELEFONO="Telefono"
            val COL_NOMBRE="Nombre"
            val COL_APELLIDOP="Apellido_P"
            val COL_APELLIDOM="Apellido_M"
            val COL_CONTRASENA="Contrasena"
            val COL_EMAIL="Email"

        }
    }

    abstract  class ExerciseInfo{

        companion object{
            val TABLE_NAME="ejercicios"
            val COL_ID="Id_Ejercicio"
            val COL_NOMBRE="Nombre"
            val COL_IMAGEN="IMAGEN"
            val COL_IDCATEGORIA="Id_Categoria"
        }
    }

    abstract class FavoriteRoutinesInfo{

        companion object{
            val TABLE_NAME="rutinas"
            val COL_IDRUTINA="Id_Rutina"
            val COL_NOMBRE="Nombre"
            val COL_IDUSUARIO="Id_Usuario"
            val COL_AUTOR="Autor"
            val COL_CATEGORIAS="Categorias"
            val COL_EMAIL="Email"
            val COL_IMAGEN="Imagen"
            val COL_USERLOGGED="User"
        }
    }

    abstract class RoutinesExercisesInfo{

        companion object{
            val TABLE_NAME="Rutinas_Ejercicios"
            val COL_IDRUTINA="Id_Rutina"
            val COL_IDEJERCICIO="Id_Ejercicio"
            val COL_NOMBRE="Nombre"
            val COL_REPETICIONES="Repeticiones"
            val COL_SERIES="Series"
            val COL_IMAGEN="Imagen"
        }
    }

}