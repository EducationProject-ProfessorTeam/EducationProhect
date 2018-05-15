package com.filter;

import com.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLogedInFilter implements Filter {

    private String userLoginURL;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String requestURL = request.getRequestURI();
        String[] url = requestURL.split("/");
//        通过会话获取用户
        User user = (User) session.getAttribute("user");
//        对已经登录的用户和userLogin路径进行放行
        if (user != null || url[3].equals(userLoginURL)){
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/sign.html");
        }

    }

    public void init(FilterConfig config) throws ServletException {
        userLoginURL = config.getInitParameter("userLoginURL");
    }

}
