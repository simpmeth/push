package com.alerter.zapsibkombank;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebService {

    static final String COOKIES_HEADER = "Set-Cookie";
    static final String COOKIE = "Cookie";

    public static CookieManager msCookieManager = new CookieManager();

    private static int responseCode;

    public static String    sendPost(String requestURL, String urlParameters,Map<String,String> headers) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            /*TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };


            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
*/
            HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.getPermission();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            //conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if (!headers.isEmpty()){

                for (Map.Entry<String, String> header : headers.entrySet()){
                    conn.addRequestProperty(header.getKey(),header.getValue());

                }
            }
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn.setRequestProperty(COOKIE ,
                        TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
            }

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            if (urlParameters != null) {
                writer.write(urlParameters);
            }
            writer.flush();
            writer.close();
            os.close();

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            setResponseCode(conn.getResponseCode());

            if (getResponseCode() == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    // HTTP GET request
    public static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla");

        /*
         * https://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
         * Get Cookies form cookieManager and load them to connection:
         */
        if (msCookieManager.getCookieStore().getCookies().size() > 0) {
            //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
            con.setRequestProperty(COOKIE ,
                    TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
        }

        /*
         * https://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
         * Get Cookies form response header and load them to cookieManager:
         */
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }


        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static void setResponseCode(int responseCode) {
        WebService.responseCode = responseCode;
        Log.i("Milad", "responseCode" + responseCode);
    }


    public static int getResponseCode() {
        return responseCode;
    }
}
