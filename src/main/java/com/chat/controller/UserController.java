package com.chat.controller;

import com.chat.service.UserService;
import com.chat.vo.ResultData;
import com.chat.vo.dto.CompleteInfoDTO;
import com.chat.vo.dto.CreateAccountDTO;
import com.chat.vo.dto.LoginDTO;
import com.chat.vo.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 创建账号
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/createAccount")
    public ResultData createAccount(@RequestBody CreateAccountDTO dto, HttpServletRequest request){
        String token = request.getHeader("token");
        String deviceId = request.getHeader("deviceId");
        return userService.createAccount(dto.geteMail(),dto.getPassword(),dto.getCode(),token,deviceId);
    }

    /**
     * 完善账号信息
     * @param request
     * @param dto
     * @return
     * @throws IOException
     */
    @RequestMapping("/completeInfo")
    public ResultData completeInfo(HttpServletRequest request, @RequestBody CompleteInfoDTO dto) throws IOException {
        String token = request.getHeader("token");
        return userService.completeInfo(token,dto.getHeadPortraits(),dto.getName());
    }

    /**
     * 账号密码登录
     * @param loginDTO
     * @param request
     * @return
     */
    @RequestMapping("/loginByPass")
    public ResultData loginByPass(@RequestBody LoginDTO loginDTO, HttpServletRequest request){
        String deviceId = request.getHeader("deviceId");
        return userService.loginByPass(loginDTO.geteMail(),loginDTO.getPassword(),deviceId);
    }

    /**
     * 刷新token
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/refresh")
    public ResultData refresh(@RequestBody TokenDTO dto, HttpServletRequest request){
        String deviceId = request.getHeader("deviceId");
        return userService.refresh(dto.getAccessToken(),dto.getRefreshToken(),deviceId);
    }


}
