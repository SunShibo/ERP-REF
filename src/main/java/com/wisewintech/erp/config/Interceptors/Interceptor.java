package com.wisewintech.erp.config.Interceptors;

import com.wisewintech.erp.entity.UserBO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Component
public class Interceptor implements HandlerInterceptor {
 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       /* ServletOutputStream outputStream = response.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("{\"user\":\"test\"}");
        outputStreamWriter.flush();
        outputStreamWriter.close();*/
        HttpSession session = request.getSession();
        UserBO user = (UserBO) session.getAttribute("user");
        if (user == null){
            return true;
        }else {
            return true;
        }
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}