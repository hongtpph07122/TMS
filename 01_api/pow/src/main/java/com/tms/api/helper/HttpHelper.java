package com.tms.api.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.dto.DurationDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by dinhanhthai on 30/06/2019.
 */
public class HttpHelper {

    public static <T> T getResponse(String url, Class<T> tClass) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        T dto = mapper.readValue(content, tClass);
        return dto;
    }
/*
    public static <T> T postResponse(String url, String token, Class<T> tClass, StringEntity params) throws IOException {
        HttpPost request = createPostRequest(url, token, params);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        T obj = mapper.readValue(content, tClass);

        return obj;
    }*/

    private static HttpPost createPostRequest(String url, StringEntity params) {
        HttpPost request = new HttpPost(url);
        //StringEntity params = new StringEntity(json.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        return request;
    }

    public static <T> T postResponse(String url, Class<T> tClass, StringEntity params) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = createPostRequest(url, params);
        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        T obj = mapper.readValue(content, tClass);

        return obj;
    }

    public static String postResponse(String url, StringEntity params) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = createPostRequest(url, params);
        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());

        return content;
    }

    public static List<DurationDTO> postResponseDuration(String url, StringEntity params) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = createPostRequest(url, params);
        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        List<DurationDTO> result = mapper.readValue(content, new TypeReference<List<DurationDTO>>(){});

        return result;
    }


    public static HttpResponse ghnGetOrderInfoPostResponse(String url, StringEntity params) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        return response;
    }

    public static HttpPost createPostRequestWithApikey(String url, String apiKey, StringEntity params) {
        HttpPost request = new HttpPost(url);
        //StringEntity params = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        request.addHeader("content-type", "application/json;charset=UTF-8");
        if (apiKey != null && apiKey != "")
            request.addHeader("api-key", apiKey);
        if (params != null) {
            params.setContentEncoding(HTTP.UTF_8);
//            StringEntity entity = new StringEntity(params, "UTF-8");
            request.setEntity(params);
        }

        return request;
    }

}
