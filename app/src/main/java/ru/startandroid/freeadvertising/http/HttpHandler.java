package ru.startandroid.freeadvertising.http;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }
    public String ReadHttpResponse(String url){
        StringBuilder sb= new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            request.addHeader("login", "login");
            HttpResponse response = client.execute(request);
            StatusLine sl = response.getStatusLine();
            int sc = sl.getStatusCode();
            if (sc==200)
            {
                HttpEntity ent = response.getEntity();
                InputStream inpst = ent.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inpst));
                String line;
                while ((line=rd.readLine())!=null)
                {
                    sb.append(line);
                }
                return sb.toString();
            }
            else
            {
                Log.e( "log_tag","I didn't  get the response!");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ReadHttpResponsewithHeader(String url, String headername, String header){
        StringBuilder sb= new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            request.addHeader(headername, header);
            HttpResponse response = client.execute(request);
            StatusLine sl = response.getStatusLine();
            int sc = sl.getStatusCode();
            if (sc==200)
            {
                HttpEntity ent = response.getEntity();
                InputStream inpst = ent.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inpst));
                String line;
                while ((line=rd.readLine())!=null)
                {
                    sb.append(line);
                }
                return sb.toString();
            }
            else
            {
                Log.e( "log_tag","I didn't  get the response!");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean deleteQuery(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpDelete request = new HttpDelete(url);
        try {
            HttpResponse response = client.execute(request);
            StatusLine sl = response.getStatusLine();
            int sc = sl.getStatusCode();
            if (sc==200)
                return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean postQuery(String url, Map<String, String> pairs, String fileAdress){
//        HttpClient client = new DefaultHttpClient();
//        HttpPost postRequest = new HttpPost(url);
//        postRequest.
//        postRequest.addHeader("Content-type", "multipart/form-data");
//        try {
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            for (String key: pairs.keySet()) {
//                builder.addTextBody(key, pairs.get(key));
//            }
//
//
////        if (fileAdress != null && !fileAdress.equals(""))
////            builder.addPart("file", new FileBody(new File(fileAdress)));
//
////            postRequest.setEntity(builder.build());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            HttpResponse response = client.execute(postRequest);
//            StatusLine sl = response.getStatusLine();
//            int sc = sl.getStatusCode();
//            if (sc==200) {
//                client.getConnectionManager().shutdown();
//                return true;
//            }
//            client.getConnectionManager().shutdown();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    public boolean postQuery3(String urlTo, HashMap<String, String> parmas, String filepath) {
        HttpPost post = new HttpPost(urlTo);
        HttpClient hc = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Iterator<String> keys = parmas.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = parmas.get(key);
            nameValuePairs.add(new BasicNameValuePair(key, value));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            post.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            HttpResponse rp = hc.execute(post);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    public boolean postWithOneFile(String urlTo, HashMap<String, String> params, String filePath) {
        try {
            System.out.println("Urlllllllll="+urlTo);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            for (String key : params.keySet()) {
                entityBuilder.addTextBody(key, params.get(key));
            }
            if (filePath != null && !filePath.equals("")) {
                entityBuilder.addBinaryBody("file", new File(filePath));
            }
            HttpPost post = new HttpPost(urlTo);
//            post.addHeader("User-Agent", "Test");
//            post.addHeader("Content-type", "multipart/form-data");
//            post.addHeader("Accept", "image/jpg");

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            System.out.println("ressssssssssssssssssssssssssssssss"+response.getStatusLine().getStatusCode()+"+++++++++++++++++++++++++********");
            System.out.println("OKKKKKKKKKKKKKKKKKKKKKK");
            if (response.getStatusLine().getStatusCode()==200)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean postQuery(String urlTo, HashMap<String, String> params, String filepath) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;

        try {
            System.out.println(filepath + " ----------");
            System.out.println("urllllllllllllllllllll="+urlTo);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

//            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty( "charset", "utf-8");

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            if (filepath != null && !filepath.equals("")) {
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + filepath + "\"" + lineEnd);
//            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                outputStream.writeBytes(lineEnd);
            }


            if (filepath != null && !filepath.equals("")) {
                System.out.println(filepath+"  *******");
                File file = new File(filepath);
                FileInputStream fileInputStream = new FileInputStream(file);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
            }
            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            for (String key : params.keySet()) {
                String value = params.get(key);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeUTF(value);
                outputStream.writeBytes(lineEnd);
            }
            System.out.println(filepath+"   9999999999999999999999");
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            System.out.println(connection.getResponseCode()+"  ++++++++++++++++++++++");
            if (200 != connection.getResponseCode()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean postQueryWithFiles(String urlTo, HashMap<String, String> parmas, ArrayList<String>filepaths) {

        System.out.println("URLLLLLLLLLLLLLLLLLLLLLL="+urlTo);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPostRequest = new HttpPost(urlTo);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        ContentType contentType = ContentType.create(
                HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

//        FileBody[]fileBodies = new FileBody[filepaths.size()];
//        for (int i = 0; i < fileBodies.length; i++) {
//            fileBodies[i] = new FileBody(new File(filepaths.get(i)), "image/jpeg");
//        }

        for (String key : parmas.keySet()) {
            builder.addTextBody(key, parmas.get(key), contentType);
        }

//        for (int i = 0; i < fileBodies.length; i++) {
//            builder.addPart("file["+i+"]", fileBodies[i]);
//        }
        httpPostRequest.setEntity(builder.build());
        try {
            HttpResponse response = httpClient.execute(httpPostRequest);
            InputStream instreanm = response.getEntity().getContent();
            String result = "NOT WORKING";
            if (instreanm != null)
                result = convertInputStreamToString(instreanm);
            System.out.println(result+"             ----------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
//    public boolean postQuery2(String urlTo, HashMap<String, String> parmas, ArrayList<String>filepaths) {
//        HttpURLConnection connection = null;
//        DataOutputStream outputStream = null;
//
//        String twoHyphens = "--";
//        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
//        String lineEnd = "\r\n";
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 10 * 1024 * 1024;
//
//        try {
//
//            URL url = new URL(urlTo);
//            connection = (HttpURLConnection) url.openConnection();
//
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setUseCaches(false);
//
////            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//            outputStream = new DataOutputStream(connection.getOutputStream());
//            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//            int i = 0;
//            for (String filepath : filepaths) {
//                if (filepath != null && !filepath.equals("")) {
//                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "file["+i+"]" + "\"; filename=\"" + filepath + "\"" + lineEnd);
////                  outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
//                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
//                    outputStream.writeBytes(lineEnd);
//                    i++;
//                }
//
//                if (filepath != null && !filepath.equals("")) {
//                    System.out.println(filepath + "  *******");
//                    File file = new File(filepath);
//                    FileInputStream fileInputStream = new FileInputStream(file);
//
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    buffer = new byte[bufferSize];
//
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                    while (bytesRead > 0) {
//                        outputStream.write(buffer, 0, bufferSize);
//                        bytesAvailable = fileInputStream.available();
//                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                    }
//                    outputStream.writeBytes(lineEnd);
//                }
//            }
//            // Upload POST Data
//            Iterator<String> keys = parmas.keySet().iterator();
//            while (keys.hasNext()) {
//                String key = keys.next();
//                String value = parmas.get(key);
//                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
//                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
//                outputStream.writeBytes(lineEnd);
//                outputStream.writeBytes(value);
//                outputStream.writeBytes(lineEnd);
//            }
//            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            System.out.println(connection.getResponseCode()+"  ++++++++++++++++++++++");
//            if (200 != connection.getResponseCode()) {
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//    public boolean postQuery(String urlTo, Map<String, String> parmas, String filepath) throws IOException {
//
//        String charset = "UTF-8";
//        String requestURL = urlTo;
//
//        MultipartUtility multipart = new MultipartUtility(requestURL, charset);
//        for (String key : parmas.keySet()) {
//            multipart.addFormField(key, parmas.get(key));
//        }
//        if (filepath != null && !filepath.equals(""))
//            multipart.addFilePart("file", new File(filepath));
//        String response = multipart.finish(); // response from server.
//        if (response != null && !response.equals(""))
//            return true;
//        return false;
//    }
//}

//    public boolean postQuery(String requestURL, HashMap<String, String> postDataParams, String filepath) throws IOException {
//
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
//
//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
//            nameValuePairs.add(new BasicNameValuePair("vin", postDataParams.get("vin")));
////            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//
//            StatusLine sl = response.getStatusLine();
//            int sc = sl.getStatusCode();
//            if (sc==200) {
//                httpclient.getConnectionManager().shutdown();
//                return true;
//            }
//
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }
//        return false;
//    }
//    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//        for(Map.Entry<String, String> entry : params.entrySet()){
//            if (first)
//                first = false;
//            else
//                result.append("&");
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
//        return result.toString();
//    }

}

