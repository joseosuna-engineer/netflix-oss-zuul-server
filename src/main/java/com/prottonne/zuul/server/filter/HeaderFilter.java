package com.prottonne.zuul.server.filter;

import com.google.common.net.HttpHeaders;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class HeaderFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HeaderFilter() {
        super();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        try {
            String ipRaw = context.getRequest().
                    getHeader(HttpHeaders.X_FORWARDED_FOR);

            logger.info("IP_RAW={}", ipRaw);

            String ip = getIp(ipRaw);

            logger.info("IP={}", ip);

            String path = context.getRequest().getRequestURI();

            logger.info("PATH={}", path);

            String authToken = context.getRequest().
                    getHeader(HttpHeaders.AUTHORIZATION);

            logger.info("TOKEN={}", authToken);

            // What you want to add to headers
            context.addZuulRequestHeader(HttpHeaders.X_USER_IP,
                    ip);

        } catch (RuntimeException e) {

            logger.error("ZUUL ERROR", e);

            context.setResponseStatusCode(HttpStatus.ACCEPTED.value());
            context.setSendZuulResponse(Boolean.FALSE);
            context.getResponse().addHeader(HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_JSON_VALUE);

            // what you want to response to your callback in json format
            context.setResponseBody(
                    "{\"code\": \""
                    + "1"
                    + "\"}"
            );

        }
        return new Object();
    }

    private String getIp(String ipRaw) {
        String[] arr = ipRaw.split(",");
        return arr[0];
    }

}
