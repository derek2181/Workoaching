package com.example.workoaching.models

data class  ChangePasswordData(var context:String? = null,
                               var email :String? = null,
                                var currentPassword : String? = null,
                                var newPassword : String? = null) {
}