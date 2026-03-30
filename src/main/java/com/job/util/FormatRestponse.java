package com.job.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.job.domain.RestResponse;
import com.job.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
       return true;
    }
    @Override
    @Nullable
    public Object beforeBodyWrite(Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
                HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status= servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(status);
        if(body instanceof String){
          return body;
        }
              if(status>=400){
                //case error
               return body;
             
       

              }
              else{
                res.setData(body);
                ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
                res.setMessage(apiMessage != null ? apiMessage.value() : "Call api success");

                //case success
         
              }
              return res;

    }
    
}
