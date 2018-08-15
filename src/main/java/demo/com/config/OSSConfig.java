package demo.com.config;

public class OSSConfig {
	
	//上传附件路径
	public static final String FILE_PATH = "oss/cms";
	// endpoint以杭州为例，其它region请按实际情况填写
	public static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
	// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
	public static final String ACCESS_KEY_ID = "aaa";
	public static final String ACCESS_KEY_SECRET = "bbb";
	
	//如下参数测试环境与正式环境不同
	public static final String BUCKET_NAME = "ccc";
	public static final String APP_OSS_URL = "http://ddd.com";
	
}
