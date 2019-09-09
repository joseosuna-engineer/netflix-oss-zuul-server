package com.prottonne.zuul.server.exception;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String serviceId = "zuul-server";

    @Value("${zuul.error.general}")
    private String zuulGeneralError;

    public IndexErrorController() {
        super();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    Object error(HttpServletRequest request) {

        final Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");

        logger.error("ZUUL ERROR", exc);

        return new Object();
    }
}
