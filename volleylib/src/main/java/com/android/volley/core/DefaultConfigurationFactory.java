package com.android.volley.core;

import android.content.Context;
import android.os.Environment;

import com.android.volley.control.HttpTrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ruoyun on 2015/10/15.
 */
public class DefaultConfigurationFactory {

    public static final int DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024;

    private DefaultConfigurationFactory() {
    }

    public static String createDiskCache(Context context, String uniqueName) {
        String cachePath;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
                /** /sdcard/Android/data/<application package>/cache */
            } else {
                cachePath = context.getCacheDir().getPath();
                /** /data/data/<application package>/cache */
            }
        } catch (Exception e) {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    /**
     * http 配置https
     *
     * @param inputStream
     * @param passWord
     * @return
     */
    public static Scheme setHttpCertificates(InputStream inputStream, String passWord) {
        if (inputStream == null) return null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(inputStream, passWord.toCharArray());
            SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore, passWord, trustStore);
            Scheme sch = new Scheme("https", socketFactory, 443);
            return sch;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SSLContext setCertificates(InputStream... certificates) {
        return setCertificates(certificates, null, null);
    }

    /**
     * httpurlconnection 配置https
     *
     * @param certificates
     * @param bksFile
     * @param password
     * @return
     */
    public static SSLContext setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(keyManagers, new TrustManager[]{new HttpTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)

                {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

}
