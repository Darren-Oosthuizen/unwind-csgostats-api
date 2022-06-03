package csgo.stats.parser.csgoapi.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private ObjectMapper jsonMapper;
    private String className;
    private final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    public LoggingRequestInterceptor(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        className = this.getClass().getSimpleName();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response, request.getURI());
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        String bodyUTF8 = new String(body, StandardCharsets.UTF_8);
        logger.info("=========================== Request Begin ================================================");
        logger.info("URI         : " + request.getURI());
        logger.info("Method      : " + request.getMethod());
        logger.info("Headers     : " + request.getHeaders());
        logger.info("Request body: " + bodyUTF8);
        logger.info("========================== Request End ================================================");
    }

    private void traceResponse(ClientHttpResponse response, URI uri) throws IOException {
        String body;
        //Check if response is of content type image then don't log.
        if (response.getHeaders() != null &&
                response.getHeaders().get("Content-Type") != null &&
                response.getHeaders().get("Content-Type").get(0) != null &&
                !response.getHeaders().get("Content-Type").get(0).toLowerCase().contains("json") &&
                !response.getHeaders().get("Content-Type").get(0).toLowerCase().contains("html")) {
            body = "BINARY";
        } else {
            StringBuilder inputStringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
            body = inputStringBuilder.toString();
        }
        logger.info("=========================== Response Begin ================================================");
        logger.info("URI           : " + uri);
        logger.info("Status code   : " + response.getStatusCode());
        logger.info("Headers       : " + response.getHeaders());
        logger.info("Response body : " + body);
        logger.info("========================== Response End ================================================");
    }

}
