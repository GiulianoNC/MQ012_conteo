package com.quantum.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Output {

    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("6")
    @Expose
    private Boolean _6;

    public String get3() {
        return _3;
    }

    public void set3(String _3) {
        this._3 = _3;
    }

    public Boolean get6() {
        return _6;
    }

    public void set6(Boolean _6) {
        this._6 = _6;
    }
}
