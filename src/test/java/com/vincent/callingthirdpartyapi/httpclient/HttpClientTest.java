package com.vincent.callingthirdpartyapi.httpclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.java_api.APOD;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author vincent
 */
public class HttpClientTest {

    @Test
    void officialCasesTest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // HTTP GET Example
        HttpGet httpGet = new HttpGet("http://targethost/homepage");
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        }

        // HTTP POST Example
        HttpPost httpPost = new HttpPost("http://targethost/login");
        List<NameValuePair> nvps = Lists.newArrayList();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        }
    }

    @Test
    void fluentApiTest() throws IOException {
        // The fluent API relieves the user from having to deal with manual deallocation of system
        // resources at the cost of having to buffer response content in memory in some cases.
        Request.Get("http://targethost/homepage")
                .execute()
                .returnContent();

        Request.Post("http://targethost/login")
                .bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
                .execute()
                .returnContent();
    }

    @Test
    void httpclientGetTest() throws IOException {
        /*
         * https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY，访问后的 json 数据格式如下：
         * {
         *   "date": "2020-11-17",
         *   "explanation": "What's creating these long glowing streaks in the sky? No one is sure.  Known as Strong Thermal Emission Velocity Enhancements (STEVEs), these luminous light-purple sky ribbons may resemble regular auroras, but recent research reveals significant differences. A STEVE's great length and unusual colors, when measured precisely, indicate that it may be related to a subauroral ion drift (SAID), a supersonic river of hot atmospheric ions thought previously to be invisible.  Some STEVEs are now also thought to be accompanied by green picket fence structures, a series of sky slats that can appear outside of the main auroral oval that does not involve much glowing nitrogen. The featured wide-angle composite image shows a STEVE in a dark sky above Childs Lake, Manitoba, Canada in 2017, crossing in front of the central band of our Milky Way Galaxy.",
         *   "hdurl": "https://apod.nasa.gov/apod/image/2011/SteveMilkyWay_NasaTrinder_6144.jpg",
         *   "media_type": "image",
         *   "service_version": "v1",
         *   "title": "A Glowing STEVE and the Milky Way",
         *   "url": "https://apod.nasa.gov/apod/image/2011/SteveMilkyWay_NasaTrinder_960.jpg"
         * }
         */
        ObjectMapper mapper = new ObjectMapper();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");
            APOD response = client.execute(request, httpResponse -> mapper.readValue(httpResponse.getEntity().getContent(), APOD.class));
            System.out.println("APOD ->" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
            System.out.println("APOD.title -> " + response.title);// A Glowing STEVE and the Milky Way
        }
    }

    /**
     * 无参 get 请求
     *
     * @throws IOException
     */
    @Test
    void doGet() throws IOException {
        String uri = "http://www.baidu.com";

        //普通写法
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println("StatusLine：" + response.getStatusLine());
            System.out.println("StatusCode：" + response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            System.out.println("entity：" + EntityUtils.toString(entity, StandardCharsets.UTF_8));
        }

        //链式调用
        String content = Request.Get(uri)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
        System.out.println("Content：" + content);
    }

    /**
     * 带参 get 请求
     *
     * @throws IOException
     */
    @Test
    void doGetWithParameter() throws Exception {
        URI uri = new URIBuilder("http://www.baidu.com/s")
                .addParameter("wd", "java")
                .build();

        //普通写法
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (Objects.equals(response.getStatusLine().getStatusCode(), 200)) {
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity, StandardCharsets.UTF_8));
            }
        }

        //链式调用
        String content = Request.Get(uri)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
        System.out.println("Content：" + content);
    }

    /**
     * 普通 post 请求
     *
     * @throws IOException
     */
    @Test
    void doPost() throws IOException {
        /*
         * http://api.k780.com/?app=weather.today&weaid=1&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json 访问后的 json 数据格式如下：
         * {
         *   "success" : "1",
         *   "result" : {
         *     "weaid" : "1",
         *     "days" : "2020-11-18",
         *     "week" : "星期三",
         *     "cityno" : "beijing",
         *     "citynm" : "北京",
         *     "cityid" : "101010100",
         *     "temperature" : "10℃/4℃",
         *     "temperature_curr" : "10℃",
         *     "humidity" : "96%",
         *     "aqi" : "92",
         *     "weather" : "中雨转小雨",
         *     "weather_curr" : "雨",
         *     "weather_icon" : "http://api.k780.com/upload/weather/d/7.gif",
         *     "weather_icon1" : "",
         *     "wind" : "北风",
         *     "winp" : "2级",
         *     "temp_high" : "10",
         *     "temp_low" : "4",
         *     "temp_curr" : "10",
         *     "humi_high" : "0",
         *     "humi_low" : "0",
         *     "weatid" : "41",
         *     "weatid1" : "",
         *     "windid" : "8",
         *     "winpid" : "2",
         *     "weather_iconid" : "7"
         *   }
         * }
         */
        String uri = "http://api.k780.com/?app=weather.today&weaid=1&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        ObjectMapper mapper = new ObjectMapper();

        //普通写法
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        try (final CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (Objects.equals(response.getStatusLine().getStatusCode(), 200)) {
                String responseEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JsonNode jsonNode = mapper.readTree(responseEntity);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
            }
        }

        //链式调用
        String context = Request.Post(uri)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
        JsonNode jsonNode = mapper.readTree(context);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
    }

    /**
     * 带参 post 请求
     *
     * @throws IOException
     */
    @Test
    void doPostWithParameter() throws Exception {
        String uri = "http://api.k780.com";
        ObjectMapper mapper = new ObjectMapper();

        //普通写法
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        //伪装浏览器请求
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");

        //设置请求参数
        List<NameValuePair> list = Lists.newArrayList();
        list.add(new BasicNameValuePair("app", "weather.today"));
        list.add(new BasicNameValuePair("weaid", "1"));
        list.add(new BasicNameValuePair("appkey", "10003"));
        list.add(new BasicNameValuePair("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4"));
        list.add(new BasicNameValuePair("format", "json"));
        httpPost.setEntity(new UrlEncodedFormEntity(list));

        try (final CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (Objects.equals(response.getStatusLine().getStatusCode(), 200)) {
                String responseEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JsonNode jsonNode = mapper.readTree(responseEntity);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
            }
        }

        //链式调用
        String context = Request.Post(uri)
                .setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36")
                .bodyForm(Form.form()
                        .add("app", "weather.today")
                        .add("weaid", "1")
                        .add("appkey", "10003")
                        .add("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4")
                        .add("format", "json")
                        .build())
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
        JsonNode jsonNode = mapper.readTree(context);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
    }
}

















