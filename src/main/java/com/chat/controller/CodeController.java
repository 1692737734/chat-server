package com.chat.controller;

import com.chat.service.CodeService;
import com.chat.vo.ResultData;
import com.chat.vo.dto.GetCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @RequestMapping("/getRegisterEmailCode")
    public ResultData getRegisterEmailCode(@RequestBody GetCodeDTO dto, HttpServletRequest request) throws MessagingException {
        String deviceId = request.getHeader("deviceId");
        return codeService.getRegisterCode(dto.geteMail(),deviceId);
    }


}
