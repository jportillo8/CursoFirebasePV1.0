package com.jportillo8.criptotraiderld.network

import java.lang.Exception

interface RealtimeDataListener <T>{

    //17. Nos notifica cada vez que halla un cambio
    fun onDataChange(updateData: T)
    fun onError(exception: Exception)
}
//17..