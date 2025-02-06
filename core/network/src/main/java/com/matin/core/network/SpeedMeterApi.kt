package com.matin.core.network

import com.matin.core.network.model.NetworkPlayers
import retrofit2.http.GET

interface SpeedMeterApi {
    @GET("?seed=empatica&inc=name,picture&gender=male&results=10&noinfo")
    suspend fun getPlayers(): NetworkPlayers
}