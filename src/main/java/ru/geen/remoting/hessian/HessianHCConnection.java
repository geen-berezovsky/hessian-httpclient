package ru.geen.remoting.hessian;

import com.caucho.hessian.client.HessianConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;

import java.io.*;

/**
 * Internal connection to a server based on the {@link org.apache.http.client.HttpClient}
 * User: geen
 * Date: 07.06.14
 * Time: 16:40
 */
public class HessianHCConnection implements HessianConnection {

    private final HttpClient httpClient;

    private final HttpPost httpPost;

    private HttpResponse response;

    public HessianHCConnection(HttpClient httpClient, HttpPost httpPost) {
        this.httpClient = httpClient;
        this.httpPost = httpPost;

    }

    @Override
    public void addHeader(String key, String value) {
        httpPost.addHeader(key, value);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        PipedInputStream pin = new PipedInputStream();
        HttpEntity entity = new InputStreamEntity(pin);
        httpPost.setEntity(entity);

        PipedOutputStream pout = new PipedOutputStream();
        pout.connect(pin);

        return pout;
    }

    @Override
    public void sendRequest() throws IOException {
        response = httpClient.execute(httpPost);
    }

    @Override
    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public String getStatusMessage() {
        return response.getStatusLine().getReasonPhrase();
    }

    @Override
    public String getContentEncoding() {
        return response.getEntity().getContentEncoding().getValue();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return response.getEntity().getContent();
    }

    @Override
    public void close() throws IOException {
        //TODO geen:07.06.2014:release resources
    }

    @Override
    public void destroy() throws IOException {
        //TODO geen:07.06.2014:release resources
    }
}
