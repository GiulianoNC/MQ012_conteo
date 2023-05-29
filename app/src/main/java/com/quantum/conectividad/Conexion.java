package com.quantum.conectividad;

import com.quantum.parseo.Cuerpo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Conexion {

    //default o  respuesta 1
    @POST("/jderest/v3/orchestrator/MQ1201A_ORCH")
    Call<Cuerpo> getDatos(@Body Cuerpo users );

    //si la respuesta es 2
    @POST("/jderest/v3/orchestrator/MQ1202A_ORCH")
    Call<Cuerpo> getDatos2(@Body Cuerpo users );

    //si la respuesta es 3
    @POST("/jderest/v3/orchestrator/MQ1203A_ORCH")
    Call<Cuerpo> getDatos3(@Body Cuerpo users );

    //si la respuesta es 4
    @POST("/jderest/v3/orchestrator/MQ1204A_ORCH")
    Call<Cuerpo> getDatos4(@Body Cuerpo users );

    //si la respuesta es 5
    @POST("/jderest/v3/orchestrator/MQ1205A_ORCH")
    Call<Cuerpo> getDatos5(@Body Cuerpo users );

    //si la respuesta es 6
    @POST("/jderest/v3/orchestrator/MQ1206A_ORCH")
    Call<Cuerpo> getDatos6(@Body Cuerpo users );

    //si la respuesta es 7
    @POST("/jderest/v3/orchestrator/MQ1207A_ORCH")
    Call<Cuerpo> getDatos7(@Body Cuerpo users );

    //si la respuesta es 8
    @POST("/jderest/v3/orchestrator/MQ1208A_ORCH")
    Call<Cuerpo> getDatos8(@Body Cuerpo users );

    //si la respuesta es 9
    @POST("/jderest/v3/orchestrator/MQ1209A_ORCH")
    Call<Cuerpo> getDatos9(@Body Cuerpo users );

    //si la respuesta es 10
    @POST("/jderest/v3/orchestrator/MQ12010A_ORCH")
    Call<Cuerpo> getDatos10(@Body Cuerpo users );


}
