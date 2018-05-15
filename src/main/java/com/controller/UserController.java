package com.controller;

import com.domain.User;
import com.service.UserService;
import com.utils.HttpSessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
//@RequestMapping("")
public class UserController {
//    自动注入
    @Resource(name = "userServiceImpl")
    private UserService userService;

//    用户登录
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView userLogin(@RequestParam(value = "username", required = true) String username,
                                  @RequestParam(value = "password", required = true) String password){
//        新建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
//        新建空的User对象
        User user = new User();
//        将页面传过来的属性赋值给对象
        user.setUsername(username);
        user.setPassword(password);
//        调用业务查询接口查询是否有此用户
        String userId = userService.getUserIdByUsernameAndPassword(user);
        System.out.println(userId);
        try {
            if (userId == null || userId.length() <= 0) {

            } else {
                user.setUserId(userId);
                // 以下操作获得session
                HttpSessionUtil httpSessionUtil = new HttpSessionUtil();
                HttpSession session = httpSessionUtil.getHttpSession();
                // 将对象user设置到session
                session.setAttribute("user", user);
//                成功则跳转到欢迎页面
//                  将user对象设置到model中
                modelAndView.addObject("user", user);
                modelAndView.setViewName("index.html");
            }
        }catch (Exception e){
//            捕获到任何异常则跳转到异常页面
            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/ajaxUserLogin", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> ajaxUserLogin(@RequestParam(value = "username", required = true) String username,
                                             @RequestParam(value = "password", required = true) String password){
        Map<String, String> result = new HashMap<>();
//        调用业务查询userId
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        try {
            String userId = userService.getUserIdByUsernameAndPassword(user);
            if (userId == null || userId.length() <= 0){
                result.put("eventCode", "error");
            } else {
                result.put("eventCode", "success");
                result.put("username", user.getUsername());
            }
        }catch (Exception e) {
            result.put("eventCode", "error");
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 用户注册
    */
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    public ModelAndView userRegister(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("sex") String sex ){
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId.toString());
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        userService.saveUser(user);
        modelAndView.setViewName("sign.html");
        return modelAndView;
    }

    @RequestMapping(value = "/ajaxUserLogup", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> ajaxUserLogup(@RequestParam("username") String username,
                                             @RequestParam("password") String password,
                                             @RequestParam("sex") String sex){

        Map<String, String> result = new HashMap<>();
//        调用业务查询userId
        User user = new User();
        UUID userId = UUID.randomUUID();
        try {
            String existUserId = userService.getUserIdByUsername(username);
            if (existUserId == null || existUserId.length() <= 0){
                user.setUserId(userId.toString());
                user.setUsername(username);
                user.setPassword(password);
                user.setSex(sex);
                userService.saveUser(user);
                result.put("eventCode", "success");
            }else {
                result.put("eventCode", "exist");
            }
        }catch (Exception e) {
            result.put("eventCode", "error");
            e.printStackTrace();
        }
        return result;
    }

//    在线用户
    @RequestMapping(value = "/activeUserList", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView activeUserList(){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

}
