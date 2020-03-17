package com.jportillo8.criptotraiderld.network

import java.lang.Exception

interface RealtimeDataListener <T>{

    fun onDataChange(updateData: T)
    fun onError(exception: Exception)
}