package org.codeforlancaster.gtagged;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wfaithfull on 28/11/18.
 */
public class ImageInserter {

    public static void main(String[] args) throws IOException {

        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";

        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/images?lat=54.2083723&lon=-2.8978349").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        String fn = "lancaster.jpg";

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"" + "lat" + "\"")
                    .append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    CRLF);
            writer.append(CRLF);
            writer.append("54.2083723").append(CRLF);

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"" + "lon" + "\"")
                    .append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    CRLF);
            writer.append(CRLF);
            writer.append("-2.8978349").append(CRLF);
            writer.flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fn+ "\"").append(CRLF);
            writer.append("Content-Type: image/jpeg;").append(CRLF);
            writer.append(CRLF).flush();

            IOUtils.copy(getFile(fn), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        }

        System.out.print(connection.getResponseCode());
    }

    private static InputStream getFile(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

}
