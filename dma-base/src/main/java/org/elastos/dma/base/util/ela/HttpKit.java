//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import org.elastos.dma.utility.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpKit {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static String CHARSET = "UTF-8";
    private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
    private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new TrustAnyHostnameVerifier();
    private static Logger logger = LoggerFactory.getLogger(HttpKit.class);

    private HttpKit() {
    }

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = new TrustManager[]{new TrustAnyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, tm, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void setCharSet(String charSet) {
        if (StrKit.isBlank(charSet)) {
            throw new IllegalArgumentException("charSet can not be blank.");
        } else {
            CHARSET = charSet;
        }
    }

    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection)conn).setHostnameVerifier(trustAnyHostnameVerifier);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(19000);
        conn.setReadTimeout(19000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (headers != null && !headers.isEmpty()) {
            Iterator var5 = headers.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, String> entry = (Entry)var5.next();
                conn.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }
        }

        return conn;
    }

    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
        try {
            final int TIME_OUT = 60;
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
            String s = toUrl(queryParas);
            Request.Builder builder = new Request.Builder();
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    Object value = entry.getValue();
                    if (ObjectUtils.hasBlank(value)) {
                        continue;
                    }
                    builder.addHeader(entry.getKey(), value.toString());
                }

            }
            Request request = builder.url(url + s).get().build();
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String toUrl(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("?");
        for (Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            stringBuilder.append(key).append("=").append(value.toString()).append("&");

        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
    public static String get(String url, Map<String, String> queryParas) {
        return get(url, queryParas, (Map)null);
    }

    public static String get(String url) {
        return get(url, (Map)null, (Map)null);
    }

    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;

        String var11;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "POST", headers);
            conn.connect();
            if (data != null) {
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes(CHARSET));
                out.flush();
                out.close();
            }

            var11 = readResponseString(conn);
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return var11;
    }

    public static String post(String url, Map<String, String> queryParas, String data) {
        return post(url, queryParas, data, (Map)null);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return post(url, (Map)null, data, headers);
    }

    public static String post(String url, String data) {
        return post(url, (Map)null, data, (Map)null);
    }

    private static String readResponseString(HttpURLConnection conn) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
            String line = reader.readLine();
            String var4;
            if (line == null) {
                var4 = "";
                return var4;
            } else {
                StringBuilder ret = new StringBuilder();
                ret.append(line);

                while((line = reader.readLine()) != null) {
                    ret.append('\n').append(line);
                }

                var4 = ret.toString();
                String var5 = var4;
                return var5;
            }
        } catch (Exception var15) {
            throw new RuntimeException(var15);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var14) {
                    logger.error(var14.getMessage(), var14);
                }
            }

        }
    }

    private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas != null && !queryParas.isEmpty()) {
            StringBuilder sb = new StringBuilder(url);
            boolean isFirst;
            if (url.indexOf(63) == -1) {
                isFirst = true;
                sb.append('?');
            } else {
                isFirst = false;
            }

            String key;
            String value;
            for(Iterator var4 = queryParas.entrySet().iterator(); var4.hasNext(); sb.append(key).append('=').append(value)) {
                Entry<String, String> entry = (Entry)var4.next();
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append('&');
                }

                key = (String)entry.getKey();
                value = (String)entry.getValue();
                if (StrKit.notBlank(value)) {
                    try {
                        value = URLEncoder.encode(value, CHARSET);
                    } catch (UnsupportedEncodingException var9) {
                        throw new RuntimeException(var9);
                    }
                }
            }

            return sb.toString();
        } else {
            return url;
        }
    }





    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }
}
