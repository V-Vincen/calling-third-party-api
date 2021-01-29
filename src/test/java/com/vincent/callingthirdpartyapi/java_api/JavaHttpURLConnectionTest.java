package com.vincent.callingthirdpartyapi.java_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author vincent
 */
public class JavaHttpURLConnectionTest {
    @Test
    public void t() throws IOException {
        // Create a neat value object to hold the URL
        URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");

        // Open a connection(?) on the URL(?) and cast the response(??)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();

        // Manually converting the response body InputStream to APOD using Jackson
        ObjectMapper mapper = new ObjectMapper();
        APOD apod = mapper.readValue(responseStream, APOD.class);

        // Finally we have the response
        System.out.println(apod.title);
    }

    /**
     * 以post或get方式调用对方接口方法，
     *
     * @param pathUrl
     */
    private void doPostOrGet(String pathUrl, String data) throws IOException {
        OutputStreamWriter out;

        URL url = new URL(pathUrl);
        //打开和 url 之间的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //请求方式
        conn.setRequestMethod("POST");
        //conn.setRequestMethod("GET");

        //设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

        //DoOutput 设置是否向 httpUrlConnection 输出，DoInput 设置是否从 httpUrlConnection 读入，此外发送 post 请求必须设置这两个
        conn.setDoOutput(true);
        conn.setDoInput(true);

        /*
         * 下面的三句代码，就是调用第三方 http 接口
         */
        //获取 URLConnection 对象对应的输出流
        out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
        //发送请求参数即数据
        out.write(data);
        //flush 输出流的缓冲
        out.flush();

        /*
         * 下面的代码相当于，获取调用第三方 http 接口后返回的结果
         */
        //获取 URLConnection 对象对应的输入流
        InputStream is = conn.getInputStream();

        BufferedInputStream buffer = IOUtils.buffer(is);
        System.out.println(IOUtils.toString(buffer, StandardCharsets.UTF_8));
    }

    @Test
    public void t2() throws IOException {
        /*
         *手机信息查询接口：http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=手机号
         *　　　　　　     http://api.showji.com/Locating/www.showji.com.aspx?m=手机号&output=json&callback=querycallback
         */
        doPostOrGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13026194071", "");
    }
}
