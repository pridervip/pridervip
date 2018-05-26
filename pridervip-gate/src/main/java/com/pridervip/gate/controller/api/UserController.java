package com.pridervip.gate.controller.api;

import javax.servlet.http.HttpSession;

import com.pridervip.entity.User;
import com.pridervip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;




/**
 * 注意：如果@Controller不指定其value【@Controller】，
 * 则默认的bean名字为这个类的类名首字母小写，如果指定value【@Controller(value=”UserAction”)】
 * 或者【@Controller(“UserAction”)】，
 * 则使用value作为bean的名字。
 * @author smfx1314
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/user/")
public class UserController{

    //注入userService
    @Autowired
    private UserService userService;
    /**
     * 用户登录
     *
     * @return
     */
    @RequestMapping(value="login.html")
    public ModelAndView login(String phone_mobile,String login_password,ModelAndView mv,HttpSession session) {
        User userlogin=userService.login(phone_mobile, login_password);
        if(userlogin!=null){
            //登录成功，将user对象设置到HttpSession作用范围域中
            session.setAttribute("user", userlogin);
            //转发到main请求
            /*mv.setView(new RedirectView("/smmbookapp/main"));*/
            //登录成功，跳转页面　
            mv.setViewName("/login/success ");
            return mv;
        }
        mv.setViewName("/login/login");
        return mv;
    }

    /**
     * 跳转到用户注册页面
     */
    @RequestMapping(value="login.action")
    public String registerpage() {

        return "/login/register";
    }
    /**
     * 用户注册
     */
    @RequestMapping(value="/login/register.jsp",method=RequestMethod.POST)
    public String register(User user) {
        String userPhone_mobile=user.getPhone_mobile();
        // 如果数据库中没有该用户，可以注册，否则跳转页面
        if (userService.findByUserName(userPhone_mobile) == null) {
            // 添加用户
            userService.register(user);
        // 注册成功跳转到主页面
            return "/login/login was successful.jsp";
        }else {
            // 注册失败跳转到错误页面
            return "/login/login has failed.jsp";
        }

    }
}