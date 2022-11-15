package com.example.workoaching.models



data class Routine(
    var Id_Rutina: Int? = null,
    var Nombre: String? = null,
    var Id_Usuario: Int? = null,
    var Email: String? = null,
    var Autor: String? = null,
    var Categorias: String? = null,
    var Ejercicios: List<Exercise>? = null,
    var Imagen: String? = null

){
    companion object{
        var offlineRoutinesCreated = mutableListOf<Routine>()
    }
}

data class Exercise(
    var Id_ejercicio: Int? = null,
    var Repeticiones: Int? = 1,
    var Series: Int? = 1,
    var Nombre: String? = null,
    var Imagen: String? = null,
    var Id_Categoria : Int?=null,
    var Id_Rutina  : Int?=null
)