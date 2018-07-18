package com.zlzkj.app.exception;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class AppExceptionHandler implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("ex", ex);

        if (ex instanceof UnauthenticatedException) {
            return new ModelAndView("redirect:/", model);
        } else if (ex instanceof AuthorizationException) {
            if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                try {
                    //response.setStatus(HttpStatus.OK.value()); //设置状态码
                    Map<String, Object> jsonData = new HashMap<>();
                    jsonData.put("status",-1);
                    jsonData.put("hasNoPermission",true);
                    String text = JSON.toJSONString(jsonData);

                    render(response,text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return new ModelAndView("impl/error/nonauthority", model);
            }
        } else if (ex instanceof UnknownSessionException) {
            return new ModelAndView("redirect:/", model);
        }else if(ex instanceof RuntimeException){
            try{
                render(response,ex.getMessage());
            }catch (Exception e){
                e.printStackTrace();
            }
            ex.printStackTrace();
        }else{
            ex.printStackTrace();
        }
        return null;
    }

    private void render(HttpServletResponse response, String text) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        PrintWriter pw = null;

        try {
            pw = response.getWriter();
            pw.write(text);
            pw.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        } finally {
            if(pw != null){
                pw.close();
            }
        }

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
