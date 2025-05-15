package com.spring.examenhexagonal.infrastructure.rest;

import com.spring.examenhexagonal.infrastructure.response.ResponseSunat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sunatClient",url = "https://api.apis.net.pe/v2/sunat/ruc/full")
public interface SunatClient {

    @GetMapping
    ResponseSunat getInfoSunat(@RequestParam("numero")String numero,
                               @RequestHeader("Authorization")String authorization);
}
