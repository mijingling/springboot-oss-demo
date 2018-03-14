package demo.com.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;

import demo.com.util.JsonResult;
import demo.com.util.OSSClientUtil;

@RestController
public class PicUploadController {
	private static Logger logger=Logger.getLogger(PicUploadController.class);
	@RequestMapping(value="pic/upload",method=RequestMethod.POST)
    public String imageUpload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> json = new HashMap<String, Object>();
        JsonResult result = new JsonResult("-1", "上传失败");
        MultipartFile multipartFile=((MultipartHttpServletRequest) request).getFile("imgUpload");
        try {
            // 判断是否为空 文件大小是否合适
            if (!multipartFile.isEmpty()) {
            	//上传图片
                result = OSSClientUtil.uploadImageToOSS(request, response, multipartFile);
            } else {
                result.setMsg("不能上传空文件");
            }
        } catch (Exception e) {
        	logger.error("error", e);
        } finally {
            if (result.isSuccessed()) {
                json.put("data", result.getData());
            }
            json.put("code", result.getCode());
            json.put("message", result.getMsg());
        }
        System.out.println("[返回结果]"+JSON.toJSONString(json));
        return JSON.toJSONString(json);
    }
	
}
