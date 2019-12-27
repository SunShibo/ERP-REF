package com.wisewintech.erp.controller.base;

import com.wisewintech.erp.entity.bo.AdminBO;
import com.wisewintech.erp.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class BaseCotroller {
    private static final long EXPIRY=60 * 60 * 24 *30;
    private static final String CURRENT_LOGIN_USER="currentLoginAdmin";
    @Autowired
    private RedisUtil  redis;


    /**获取登录管理员*/
    public AdminBO getLoginAdmin (HttpServletRequest request ) {
        return (AdminBO) this.getSession(request, CURRENT_LOGIN_USER) ;
    }

    /**把管理员信息存入redis*/
    public void putLoginAdmin (AdminBO admin,HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        this.putSession(createKey(admin.getId()+uuid, CURRENT_LOGIN_USER), admin);
        this.setCookie(response,CURRENT_LOGIN_USER, createKey(admin.getId()+uuid, CURRENT_LOGIN_USER));
    }


    /**
     * 删除用户this.getSession(request, CURRENT_LOGIN_USER)
     */
    public void delLoginAdmin (HttpServletRequest request,HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        String key = createKey(this.getClientLoginID(request), CURRENT_LOGIN_USER);
        redis.del(key);
        this.removeCookie(request,response,CURRENT_LOGIN_USER);
    }
    /**
     * 生成在redis中存储的key
     * @param loginId 登录用户ID
     * @param key key
     * @return
     */
    public String createKey (String loginId, String key) {
        return loginId + "@" + key ;
    }

    /**
     * 获取session
     * session存储格式为
     * loginUuid + @ + key
     * */
    public Object getSession (HttpServletRequest request , String key) {
        return redis.get(this.getClientLoginID(request));
    }
    public String getClientLoginID(HttpServletRequest request) {
        return getCookie(request , CURRENT_LOGIN_USER) ;
    }

    /**
     * session赋值
     */
    private void putSession (String key , Object value) {
        redis.set(key , value , EXPIRY);
    }

    /**
     * 设置Cookie
     * @param response
     * @param key
     * @param value
     */
    public void setCookie(HttpServletResponse response, String key , String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int)EXPIRY);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 获取Cookie
     * @param request
     * @param key
     * @return
     */
    public String getCookie(HttpServletRequest request , String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null ;
        }
        for(Cookie c :cookies ){
            if (c.getName().equals(key)) {
                return c.getValue() ;
            }
        }
        return null ;
    }

    /**
     * 删除Cookie
     * @param request
     * @param response
     * @param key
     */
    public void removeCookie (HttpServletRequest request , HttpServletResponse response , String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return  ;
        }
        for(Cookie c :cookies ){
            if (c.getName().equals(key)) {
                c.setMaxAge(0);
                c.setValue(null);
                c.setPath("/");
                response.addCookie(c);
            }
        }
    }
}