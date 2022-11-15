package com.example.workoaching.models




data class         User(
    var id_usuario: Int? = null,
    var email: String? = null,
    var nombre: String? = null,
    var apellido_p: String? = null,
    var apellido_m: String? = null,
    var telefono: String? = null,
    var direccion: String? = null,
    var imagen: String? = null,
    var contrasena: String? = null

){}