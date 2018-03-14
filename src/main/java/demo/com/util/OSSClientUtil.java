package demo.com.util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import demo.com.config.OSSConfig;

public class OSSClientUtil {
	
	private static Logger logger = Logger.getLogger(OSSClientUtil.class);
	private static final HashMap<String, String> TypeMap = new HashMap<String, String>();
	static {
        TypeMap.put("image", "gif,jpg,jpeg,png,bmp");
        TypeMap.put("flash", "swf,flv");
        TypeMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        TypeMap.put("file", "doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,dwg,pdf");
    }
	/**
     * 上传图片
     */
    public static JsonResult uploadImageToOSS(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    	JsonResult result = new JsonResult("-1", "上传失败");
    	try {
                long fileSize = 2 * 1024 * 1024;
                // 如果文件大小大于限制
                if (file.getSize() > fileSize) {
                    result.setMsg("图片过大,请选择小于2M的图片");
                    return result;
                }
                // 获取文件名字
                String originalFilename = file.getOriginalFilename();

                // 获取文件格式 jpg
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
                    result.setMsg("上传图片格式不符合规范");
                    return result;
                }
                if (!ServletFileUpload.isMultipartContent(request)) {
                    result.setMsg("上传图片失败");
                    return result;
                }
                
                // 获取文件类型
                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
                logger.info("获取文件类型："+ext);
                // 获取文件位置
                String filePath = OSSConfig.FILE_PATH + UploaderUtil.getQuickPathname(ext);
                // 文件key
                String keyFile = filePath.substring(0, filePath.lastIndexOf(".")) + "_h5Th.png";
                // 上传OSS
                int ossResult = uploadInputStream(OSSConfig.BUCKET_NAME, keyFile, file.getInputStream());
                logger.info("文件上传结果ossResult:"+ossResult);
                if(ossResult==0){
                	logger.error("上传图片处理异常");
                    result.setMsg("上传图片失败，请重新上传！");
                    return result;
                }
                logger.info("上传结束");
                // 真实路径
                String realPath = OSSConfig.APP_OSS_URL + "/";
                // 拼接全路径
                String fullPath = realPath + keyFile;
                result.setCode(JsonResult.SUCCESS);
                result.setMsg("上传成功");
                Map<String,String> data=new HashMap<>();
                data.put("fullPath", fullPath);
                result.setData(data);
        } catch (Exception e) {
            logger.error("上传图片处理异常",e);
            result.setMsg("上传图片处理异常");
        }
        return result;
    }
	
	/**
	 * 上传文件流
	 */
	private static int uploadInputStream(String bucketName,String key,InputStream fileIS) {
		int result = 0;
		logger.info("上传文件流 uploadInputStream " + bucketName + " " + key);
		// 创建OSSClient实例
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(OSSConfig.ENDPOINT, OSSConfig.ACCESS_KEY_ID, OSSConfig.ACCESS_KEY_SECRET);
			logger.info("开始上传");
			ossClient.putObject(bucketName, key, fileIS, new ObjectMetadata());
			result = 1;//成功
		} catch (Exception e) {
			logger.error("上传文件异常",e);
			result = 0;//失败
			return result;
		}
		return result;
	}
	/**
	 * 删除文件（暂未调用,注意同名文件替换，不能即时生效）
	 */
	public static void deleteFile(String bucketName,String key) {
		logger.info("删除文件 deleteFile " + bucketName + " " + key);
		// 创建OSSClient实例
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(OSSConfig.ENDPOINT, OSSConfig.ACCESS_KEY_ID, OSSConfig.ACCESS_KEY_SECRET);
			// 删除Object
			ossClient.deleteObject(bucketName, key);
		} catch (Exception e) {
			logger.error("删除文件异常",e);
		}
	}
	
}
