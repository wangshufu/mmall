package com.mmall.service.impl;

import com.github.pagehelper.StringUtil;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by wangshufu on 2017/7/27.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired//自动写入
            UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        System.out.print("userName:"+userName+"   password:"+password);
        int resultCount = userMapper.checkUsername(userName);
        System.out.print("resultCount:"+resultCount);
        if (resultCount == 0) {
            return ServerResponse.createByError("用户名不存在");
        }

        // TODO: 2017/7/27 密码需要md解密
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            return ServerResponse.createByError("密码错误");
        }

        //设置密码为null
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> checkValid = this.checkValid(user.getUsername(), Const.USERNAME);
        System.out.print("aaa:"+checkValid.isSuccess());
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        checkValid = this.checkValid(user.getEmail(), Const.EMAIL);
        System.out.print("bbb:"+checkValid.isSuccess());
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        //设置角色-->普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //将密码用MD5加密,并保存
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //插入数据库
        int insertResult = userMapper.insert(user);
        if (insertResult == 0) {
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        //isNoneBlank:表示null;"";"  "都返回false
        //isNoneEmpty:表示null;""返回false;而"   "返回true
        if (StringUtils.isNoneBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultEmailCount = userMapper.checkEmail(str);
                if (resultEmailCount > 0) {
                    return ServerResponse.createByError("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByError("参数错误");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> selectQuestion(String userName) {
        ServerResponse<String> checkValid = this.checkValid(userName, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByError("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(userName);
        if (StringUtils.isNoneBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createBySuccess("用户的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount == 0) {
            return ServerResponse.createByError("用户答案不正确");
        }
        //说明是正确的
        String token = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        //校验username
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByError("用户不存在");
        }
        //参数token是否为空
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByError("参数token不能为空");
        }
        //获取服务器保存的token和参数token进行比较
        String servletToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        //这里用的StringUtils.equals(servletToken,token):即使servletToken和token为null进行比较都不会报空指针的
        if (StringUtils.equals(servletToken, token)) {
            String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5PasswordNew);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByError("token错误或失效,请重新获取token");
        }
        return ServerResponse.createByError("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        if (StringUtils.isBlank(passwordNew)) {
            return ServerResponse.createByError("新密码不能为空");
        }
        //防止横向越权,需要校验passwordOld,一定要指定是这个用户的,不让我们直接用passwordOld去查的话,有可能查询得到的结果很多,因为多个用户的密码有可能是一样的
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount > 0) {
            //重置新密码
            user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
            int insertSelective = userMapper.insertSelective(user);
            if (insertSelective > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByError("旧密码错误");
        }
        return ServerResponse.createByError("修改密码失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //用户名是不能更改的
        //再更改信息时,还需要校验邮箱,看这个邮箱有没有被人用过
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount == 0) {
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setEmail(user.getEmail());
            updateUser.setPhone(user.getPhone());
            updateUser.setQuestion(user.getQuestion());
            updateUser.setAnswer(user.getAnswer());
            int updateResult = userMapper.insertSelective(updateUser);
            if (updateResult > 0){
                return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
            }else {
                return ServerResponse.createByError("更新信息失败");
            }
        } else {
            return ServerResponse.createByError("对不起,您的邮箱已被使用,请你换一个邮箱试试");
        }
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByError("获取个人信息失败");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


}
