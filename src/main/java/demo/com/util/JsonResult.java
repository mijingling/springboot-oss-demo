package  demo.com.util;

public class JsonResult {

	public static String SUCCESS = "0";
	/**
	 * 返回代码
	 */
	private String code ;
	
	/**
	 * 提示信息
	 */
	private String msg ;

	/**
	 * 扩展信息
	 */
	private Object data ;
	
	public JsonResult(){
	}
	
	public JsonResult(String code){
		this.code = code;
	}
	
	public JsonResult(String code,String msg){
		this.code = code;
		this.msg  = msg;
	}
	
	public boolean isSuccessed(){
		return getCode().equals(SUCCESS);
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
