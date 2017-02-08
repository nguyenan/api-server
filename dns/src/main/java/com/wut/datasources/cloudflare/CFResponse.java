package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CFResponse {
    boolean isSuccess = true;
    JsonObject cfReturn;
    JsonObject error;
    public boolean isSuccess() {
        return isSuccess ;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public JsonObject getCFReturn() {
        return cfReturn;
    }

    public void setCFReturnt(JsonObject cfReturn) {
        this.cfReturn = cfReturn;
    }

    public JsonObject getError() {
        return error;
    }

    public void setErrors(JsonObject error) {
        this.error = error;
    }


    public CFResponse(HttpResponse response) {
        StringBuilder responseString = new StringBuilder();
        BufferedReader rd;
        try {
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseString.append(line);
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfReturn = new JsonParser().parse(responseString.toString()).getAsJsonObject();

        if (!cfReturn.get("success").getAsString().equals("true")) {
            isSuccess = false;
			JsonArray errors = (JsonArray) cfReturn.get("errors");
            error = errors.get(0).getAsJsonObject();
        } 
    }
}