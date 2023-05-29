package com.quantum.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ServiceRequest1 {

    @SerializedName("submitted")
    @Expose
    private boolean submitted ;
    @SerializedName("output")
    @Expose
    private Output output  ;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("errorList")
    @Expose
    private List<Object> errorList;
    @SerializedName("diagnostics")
    @Expose
    private Object diagnostics;

    public boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<Object> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Object> errorList) {
        this.errorList = errorList;
    }

    public Object getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(Object diagnostics) {
        this.diagnostics = diagnostics;
    }


}
