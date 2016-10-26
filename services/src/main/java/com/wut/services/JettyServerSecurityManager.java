package com.wut.services;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class JettyServerSecurityManager {
	public static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			// public void checkClientTrusted(X509Certificate[] certs, String
			// authType) {}
			// public void checkServerTrusted(X509Certificate[] certs, String
			// authType) {}
			
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
		}
	}
	
	
	public static void moreSSL() throws Exception {
		// very GOOD article 
		// http://stackoverflow.com/questions/3247746/java-loading-ssl-keystore-via-a-resource
		
		System.setProperty("javax.net.ssl.keyStore", "/etc/certificates/fdms/WS1001237590._.1.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "REPLACE_ME");
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("/etc/certificates/fdms/WS1001237590._.1.ks"), "REPLACE_ME".toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, "REPLACE_ME".toCharArray());
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(kmf.getKeyManagers(), null, null);
	}
}
