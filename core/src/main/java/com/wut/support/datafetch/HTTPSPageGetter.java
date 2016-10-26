package com.wut.support.datafetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
//import javax.net.ssl.*;
//
//import javax.net.ssl.TrustManager;

public class HTTPSPageGetter {

       /**
        * Return an InputStream representing the given URL
        * Note: URL should start with https://
        * @param url
        * @return page as InputStream
        */
       public InputStream getHTTSPage(String url) {
               URL myURL;
               try {
                       myURL = new URL(url);
                       InputStream is = myURL.openStream();
                       return is;
               } catch (MalformedURLException e) {
                       e.printStackTrace();
               } catch (IOException e) {
                       e.printStackTrace();
               }
               return null;
       }

       public static void main(String args[]) {
               System.out.println("Token = " + getToken());
       }
       
       public static String getToken()
       {
               String token = null;
               try {
                       HTTPSPageGetter pageGetter = new HTTPSPageGetter();
                       String mapSeverURL =
"https://192.168.97.245:8443/imageryservice?method=getToken&username=transway&password=transway";
                       InputStream tokenSteam = pageGetter.getHTTSPage(mapSeverURL);
                       BufferedReader in = new BufferedReader(new InputStreamReader(
                                       tokenSteam));
                       token = in.readLine();
               } catch (IOException e) {
                       e.printStackTrace();
               } catch (Exception e) {
                       e.printStackTrace();
               }
               return token;
       }
       

}