package com.chat.file.utils;

import com.chat.vo.enums.FileTypeMSEnum;
import com.chat.vo.enums.ImageSuffixEnum;
import com.chat.vo.enums.OfficeSuffixEnum;
import com.chat.vo.enums.VideoSuffixEnum;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public class FileUtils {
    public static String tranName(String name,String suffix){
        String filename = (name.replace("_","") + "_"+ UUID.randomUUID().toString().replace("_","").replace("-",""))+"."+suffix;
        return filename;
    }

    /**
     * 获取客户端浏览器类型、编码下载文件名
     *
     * @param request
     * @param fileName
     * @return String
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        String rtn = "";
        try {
            String new_filename = URLEncoder.encode(fileName, "UTF8");
            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + new_filename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("msie") != -1) {
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("opera") != -1) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("safari") != -1) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                //Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.indexOf("applewebkit") != -1) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("mozilla") != -1) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /**
     * 判断是否为支持的图片格式
     * @param suffix
     * @return
     */
    public static boolean imageSupport(String suffix){
        ImageSuffixEnum imageSuffixEnum = ImageSuffixEnum.getImageSuffixEnum(suffix);
        if(imageSuffixEnum == null){
            return false;
        }
        return true;
    }

    /**
     * 判断是否为支持的文件格式
     * @param suffix
     * @return
     */
    public static boolean officeSupport(String suffix){
        OfficeSuffixEnum officeSuffixEnum = OfficeSuffixEnum.getOfficeSuffixEnum(suffix);
        if(officeSuffixEnum == null){
            return false;
        }
        return true;
    }

    /**
     * 判断是否为支持的视频格式
     * @param suffix
     * @return
     */
    public static boolean videoSupport(String suffix){
        VideoSuffixEnum videoSuffixEnum = VideoSuffixEnum.getVideoSuffixEnum(suffix);
        if(videoSuffixEnum == null){
            return false;
        }
        return true;
    }

    public static boolean officeSafe(InputStream inputStream,String suffix) throws Exception{
        byte[] b = new byte[28];
        try {
            inputStream.read(b, 0, 28);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        String pix = getFileHexString(b).substring(0,8).toUpperCase();
        String ms = FileTypeMSEnum.getFileTypeMSEnum(suffix.toUpperCase()).getValue();
        if(pix.equals(ms)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 把文件二进制流转换成十六进制数据
     * @param b
     * @return fileTypeHex
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }

        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
