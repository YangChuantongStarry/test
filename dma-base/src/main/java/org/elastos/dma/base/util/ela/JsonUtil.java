//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elastos.dma.base.exception.ApiRequestDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public JsonUtil() {
    }

    public static <T> T jsonStr2Entity(String jsonStr, Class<T> clazz) {
        if (StrKit.isBlank(jsonStr)) {
            throw new ApiRequestDataException("data can not be blank");
        } else {
            try {
                return JSONObject.parseObject(jsonStr, clazz);
            } catch (Exception var3) {
                throw new ApiRequestDataException(var3.getMessage());
            }
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap();
        if (json != null) {
            retMap = toMap(json);
        }

        return (Map)retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {

        return object;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList();

        for(int i = 0; i < array.size(); ++i) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray)value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject)value);
            }

            list.add(value);
        }

        return list;
    }

    public static String getValues(Object obj) {
        return typeOf(obj);
    }

    private static String getMapValues(Map<String, Object> src) {
        if (src != null && src.size() != 0) {
            StringBuilder sb = new StringBuilder();
            Set<String> set = src.keySet();
            Iterator it = set.iterator();

            while(it.hasNext()) {
                Object key = it.next();
                sb.append(typeOf(src.get(key)));
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    private static String getListValues(List src) {
        if (src != null && src.size() != 0) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < src.size(); ++i) {
                sb.append(typeOf(src.get(i)));
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    private static String typeOf(Object o) {
        if (o instanceof Map) {
            return getMapValues((Map)o);
        } else {
            return o instanceof List ? getListValues((List)o) : o + "";
        }
    }
}
