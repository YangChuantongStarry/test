//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;


import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil   {



    public static JSONObject httpGet(String url) {
        String s = HttpKit.get(url);

        return JSONObject.parseObject(s);
    }

    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        return httpPost(url, jsonParam, false);
    }

    public static JSONObject httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
        String post = HttpKit.post(url, jsonParam.toString());
        return JSONObject.parseObject(post);}
}
