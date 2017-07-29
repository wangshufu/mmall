package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by wangshufu on 2017/7/27.
 */
public interface IUserService {

    ServerResponse<User> login(String userName, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> selectQuestion(String userName);

    ServerResponse<String> checkAnswer(String username,String password,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String token);

    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);
}
