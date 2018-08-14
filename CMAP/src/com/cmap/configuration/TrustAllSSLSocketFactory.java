package com.cmap.configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/*
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
*/
//public class TrustAllSSLSocketFactory implements ProtocolSocketFactory {
public class TrustAllSSLSocketFactory {

	public static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{
        new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
        }
    };

    private TrustManager[] getTrustManager() {
        return TRUST_ALL_CERTS;
    }

    public Socket createSocket(final String host, final int port, final InetAddress clientHost,
                               final int clientPort) throws IOException {
        return getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    /*
    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localAddress,
                               final int localPort, final HttpConnectionParams params) throws IOException {
        return createSocket(host, port);
    }
    */

    public Socket createSocket(final String host, final int port) throws IOException {
        return getSocketFactory().createSocket(host, port);
    }

    private SocketFactory getSocketFactory() throws UnknownHostException {
        TrustManager[] trustAllCerts = getTrustManager();

        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());

            final SSLSocketFactory socketFactory = context.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
            return socketFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException exception) {
            throw new UnknownHostException(exception.getMessage());
        }
    }
}
