package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by wangshufu on 2017/7/30.
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(HttpSession session, String username, String password) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            if (response.getData().getRole() != Const.Role.ROLE_ADMIN) {
                return ServerResponse.createByError("对不起,您不是管理员用户");
            } else {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            }
        }
        return response;
    }
}
