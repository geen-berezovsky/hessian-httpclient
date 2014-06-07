package ru.geen.remoting.hessian;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianConnectionException;
import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Connection factory for creating connections to the server based on the {@link HttpClient}
 * User: geen
 * Date: 07.06.14
 * Time: 16:36
 */
public class HessianHCConnectionFactory implements HessianConnectionFactory {

    private HttpClient httpClient;

    private HessianProxyFactory hessianProxyFactory;

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void setHessianProxyFactory(HessianProxyFactory factory) {
        this.hessianProxyFactory = factory;
    }

    @Override
    public HessianConnection open(URL url) throws IOException {
        HttpPost post;
        try {
            post = new HttpPost(url.toURI());
        } catch (URISyntaxException e) {
            throw new HessianConnectionException("Cannot connect to " + url.toString(), e);
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout((int) hessianProxyFactory.getConnectTimeout())
                .setSocketTimeout((int) hessianProxyFactory.getReadTimeout())
                .build();

        post.setConfig(requestConfig);

        return new HessianHCConnection(httpClient, post);
    }
}
