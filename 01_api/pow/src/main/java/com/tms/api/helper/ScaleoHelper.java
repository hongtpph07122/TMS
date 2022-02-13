package com.tms.api.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.dto.Request.ScaleoCreateClickIdReq;
import com.tms.api.dto.Request.ScaleoCreateLeadReq;
import com.tms.api.dto.Response.ScaleoCreateClickIdRes;
import com.tms.api.dto.Response.ScaleoCreateLeadRes;
import com.tms.api.task.BasketProcessor;
import com.tms.ff.dto.response.GHTK.GHTKResponseDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScaleoHelper {

    private static final Logger logger = LoggerFactory.getLogger(ScaleoHelper.class);

    public static final String create_clickid = "click";
    public static final String protocol = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.protocol");
    public static final String host = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.host");
    public static final String key = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.apikey");

    public static final String lead_protocol = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.lead.protocol");
    public static final String lead_host = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.lead.host");
    public static final String lead_key = com.tms.ff.utils.AppProperties.getPropertyValue("api.Scaleo.lead.apikey");

//    public static final String URL = "https://demo.scaletrk.com/api/v2/network/tracker/click?api-key=bd67905aaefb9eac1bb06ede0b3f54c56d1da328";

    public static ScaleoCreateClickIdRes createClickId(ScaleoCreateClickIdReq requestDto) throws IOException {
        ScaleoCreateClickIdRes result = new ScaleoCreateClickIdRes();
        JSONObject json = new JSONObject(requestDto);
        String apiKey = "?api-key=" + key;
        String url = protocol + "://" + host + "/" + create_clickid + apiKey;
        try {
            StringEntity params = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
            result = HttpHelper.postResponse(url, ScaleoCreateClickIdRes.class, params);
            logger.info("url: {}\r\nparam: {}", url, params);

        } catch (Exception e) {
            logger.info("Create clickId error: {}", e.getMessage());
        }
        return result;
    }

    public static ScaleoCreateLeadRes createLead(ScaleoCreateLeadReq requestDto) throws IOException {
        ScaleoCreateLeadRes result = new ScaleoCreateLeadRes();
        JSONObject json = new JSONObject(requestDto);
        String apiKey = "?api-key=" + lead_key;
        String url = lead_protocol + "://" + lead_host + "/" + "leads-by-goal-id" +  apiKey;
        try {
            StringEntity params = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
            result = HttpHelper.postResponse(url, ScaleoCreateLeadRes.class, params);
            logger.info("url: {}\r\nparam: {}", url, params);

        } catch (Exception e) {
            logger.info("Create clickId error: {}", e.getMessage());
        }
        return result;
    }

}
