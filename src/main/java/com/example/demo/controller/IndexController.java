package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController extends BaseController {

    public class Users {
        public String id;
        public String  name;
    }

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "Spring Boot Index";
    }

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("title", "Spring Boot");
        return "hello";
    }

    @RequestMapping("/getmap")
    @ResponseBody
    public String getMap(@RequestParam Map<String, Object> gets) {

        return gets.toString();
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request) {
        HashMap<String, String> requests = new HashMap<>();
        requests.put("Method", request.getMethod());
        requests.put("QueryString", request.getQueryString());
        requests.put("RequestURI", request.getRequestURI());
        requests.put("getRequestURL", request.getRequestURL().toString());
        requests.put("RemoteAddr", request.getRemoteAddr());

        return request.toString();
    }

    @RequestMapping("/setcookie")
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie1 = new Cookie("cookie1", "value1");
        cookie1.setMaxAge(1800);
        Cookie cookie2 = new Cookie("cookie2", "value2");
        cookie2.setMaxAge(3600);
        response.addCookie(cookie1);
        response.addCookie(cookie2);

        return "cookie set ok";
    }

    @RequestMapping("getcookie")
    @ResponseBody
    public String getCookie(HttpServletRequest request, @CookieValue(value = "cookie1", required = false) String cookie1) {
        HashMap<String, String> map = new HashMap<>();
        Cookie[] cookies  = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: cookies) {
                map.put(cookie.getName(), cookie.getValue());
            }
        }

        return map.toString();
    }

    @RequestMapping("delcookie")
    @ResponseBody
    public String delCookie(HttpServletRequest request, HttpServletResponse respose) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                respose.addCookie(cookie);
            }
        }

        return "delete ok";
    }
    @RequestMapping("/setsession")
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("session1", "value1");;
        session.setAttribute("session2", "value2");;
        return "";
    }

    @RequestMapping("/getsession")
    @ResponseBody
    public String getSession(HttpServletRequest request, HttpSession httpSession, @SessionAttribute(value="session2", required = false) String session1) {
        HttpSession session = request.getSession();
        String session2 = (String)session.getAttribute("session2");
        String http_session1 = (String)session.getAttribute("session1");

        HashMap<String, String> sessionMap = new HashMap<>();
        Enumeration<String> sessions = session.getAttributeNames();
        while(sessions.hasMoreElements()) {
            String key = sessions.nextElement();
            sessionMap.put(key, (String)session.getAttribute(key));
        }

        return session.toString();
    }

    // learn model
    @RequestMapping("/model")
    public String model(Model model, ModelMap modelMap, Map<String, Object> map) {
        model.addAttribute("title1", "model_title");
        modelMap.addAttribute("title2", "modelMap_title");
        map.put("title2", "map_title");

        return "thymeleaf";
    }

    // ModelAndView手动渲染模板
    @RequestMapping("/modelandview")
    public ModelAndView modelAndView() {
        ModelAndView model = new ModelAndView();
        model.setViewName("hello");
        model.addObject("title1", "title1");
        model.addObject("title2", "title2");

        return model;
    }

    @RequestMapping("/getmodel")
    @ResponseBody
    public Users getModel(@ModelAttribute Users user) {
        return user;
    }
}
