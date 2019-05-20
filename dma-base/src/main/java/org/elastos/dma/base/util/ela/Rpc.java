//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Rpc {
    public Rpc() {
    }

    public static String generateBlock(int blockNum, String url) {
        Map<String, Object> map = new HashMap();
        map.put("count", String.valueOf(blockNum));
        return call("discretemining", map, url);
    }

    public static String call(String method, Map params, String url) {
        Map<String, Object> map = new HashMap();
        map.put("method", method);
        map.put("params", params);
        JSONObject jsonParam = new JSONObject(map);
        System.out.println("jsonParam = " + jsonParam);
        JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);
        System.out.println("jsonObject : " + jsonObject);
        String result = jsonObject.getString("result");
        return result;
    }

    public static String getRpcAddress(String path, String port) {
        return path + ":" + port;
    }

    public static String call_(String method, Map params, String url) {
        Map<String, Object> map = new HashMap();
        map.put("method", method);
        map.put("params", params);
        JSONObject jsonParam = new JSONObject(map);
        JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);
        return jsonObject.toString();
    }
}
