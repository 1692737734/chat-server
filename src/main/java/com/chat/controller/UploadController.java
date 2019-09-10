package com.chat.controller;

import com.chat.service.UploadService;
import com.chat.util.token.JavaWebToken;
import com.chat.vo.ResultData;
import com.chat.vo.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping("/uploadImgForCreate")
    public ResultData uploadImgForCreate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        Map<String,Object> map = JavaWebToken.verifyToken(token);
        if(map == null){
            return ResultData.instance(ResultEnum.Param_Error.getKey(),ResultEnum.Param_Error.getValue(),null);
        }
        long userId = 0;
        return uploadService.uploadImg(file,userId);
    }
}
