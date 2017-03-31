package com.example.administrator.volleydongnao.http;

import com.example.administrator.volleydongnao.http.interfaces.IHttpListener;
import com.example.administrator.volleydongnao.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * JSON HTTP的请求服务
 * Created by Administrator on 2017/1/13 0013.
 */

public class JsonHttpService implements IHttpService {
    private IHttpListener httpListener;
    private HttpClient httpClient=new DefaultHttpClient();
    private HttpPost httpPost;
    private String url;

    private byte[] requestDate;
    /**
     * httpClient获取网络的回调
     */
    private HttpRespnceHandler httpRespnceHandler=new HttpRespnceHandler();
    @Override
    public void setUrl(String url) {
        this.url=url;
    }

    /**
     * httpclient的post请求
     * 1，初始化 httpclient  & httpPost
     * 2, 将请求参数装换成ByteArrayEntity ,在通过setEntity(参数);
     * 3，执行请求  execute(httppost , 回调)
     */
    @Override
    public void excute() {
        httpPost=new HttpPost(url);
        ByteArrayEntity byteArrayEntity=new ByteArrayEntity(requestDate);
        httpPost.setEntity(byteArrayEntity);
        try {
            httpClient.execute(httpPost,httpRespnceHandler);
        } catch (IOException e) {
            httpListener.onFail();
        }
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener=httpListener;
    }

    @Override
    public void setRequestData(byte[] requestData) {
         this.requestDate=requestData;
    }
    private class HttpRespnceHandler extends BasicResponseHandler
    {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            //响应吗
            int code=response.getStatusLine().getStatusCode();
            if(code==200)
            {
                httpListener.onSuccess(response.getEntity());
            }else
            {
                httpListener.onFail();
            }


            return null;
        }
    }
}
