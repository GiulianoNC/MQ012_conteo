package com.quantum.conectividad;

import com.quantum.parseo.Cuerpo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Conexion {

    @POST("/jderest/v3/orchestrator/MQ1201A_ORCH")
    Call<Cuerpo> getDatos(@Body Cuerpo users );

}
