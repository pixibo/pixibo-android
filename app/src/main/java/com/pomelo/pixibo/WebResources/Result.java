package com.pomelo.pixibo.WebResources;


import com.pomelo.pixibo.Utils.Utils;

public interface Result {
    void getWebResponse(String result, Utils.TYPE type, int statusCode);
}
