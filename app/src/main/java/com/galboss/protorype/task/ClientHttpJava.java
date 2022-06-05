package com.galboss.protorype.task;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;

public class ClientHttpJava {

    public String multipartPostImageUser(String url, String user, String path){

        HttpPost post = new HttpPost(url);
        try{
            HttpClient client = HttpClientBuilder.create().build();
            File file = new File(path);
            String[] extension = path.split(".");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            FileBody fileBody=null;
            switch (extension[1]){
                case "png":case"PNG": {fileBody=new FileBody(file, ContentType.IMAGE_PNG);break;}
                case "jpeg":case"JPEG": {fileBody = new FileBody(file,ContentType.IMAGE_JPEG);break;}
            }
            StringBody userBody = new StringBody("user",ContentType.MULTIPART_FORM_DATA);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file",fileBody);
            builder.addPart("user",userBody);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            return response.toString();
        }catch (Exception e){

        }
        return "Error";
    }
}
