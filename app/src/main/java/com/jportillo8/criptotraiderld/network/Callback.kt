package com.jportillo8.criptotraiderld.network

import java.lang.Exception

//Nos permite mapear cualquier tipo de resultado, nos dice si la operacion fue exitosa

interface Callback <T>{
    fun onSucess(result: T?)

    fun onFailed(exception: Exception)
}