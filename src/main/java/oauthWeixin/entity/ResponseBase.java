package oauthWeixin.entity;

import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@AllArgsConstructor
@ApiModel("请求响应对象")
public class ResponseBase<T> {
	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(ResponseBase.class);

	/**
	 * 状态码
	 */
	@ApiModelProperty("消息状态码")
	private Integer rtnCode;

	/**
	 * 返回内容
	 */
	@ApiModelProperty("消息内容")
	private String msg;

	/**
	 * 数据对象
	 */
	@ApiModelProperty("数据对象")
	private T data;

	public static void main(String[] args) {
		ResponseBase<String> responseBase = new ResponseBase<>(200,"success","123456");
		System.out.println(responseBase);
		logger.info("itmayiedu...");
	}

	@Override
	public String toString() {
		return "ResponseBase [rtnCode=" + rtnCode + ", msg=" + msg + ", data=" + data + "]";
	}

	/**
	 * 初始化一个新创建的 ResponseBase 对象
	 *
	 * @param rtnCode 状态码
	 * @param msg  返回内容
	 */
	public ResponseBase(int rtnCode, String msg) {
		this.rtnCode = rtnCode;
		this.msg = msg;
	}

	/**
	 * 返回成功消息
	 *
	 * @return 成功消息
	 */
	public static ResponseBase<Void> success() {
		return ResponseBase.success("操作成功");
	}

	/**
	 * 返回成功数据
	 *
	 * @return 成功消息
	 */
	public static <T> ResponseBase<T> success(T data) {
		return ResponseBase.success("操作成功", data);
	}

	/**
	 * 返回成功消息
	 *
	 * @param msg 返回内容
	 * @return 成功消息
	 */
	public static ResponseBase<Void> success(String msg) {
		return ResponseBase.success(msg, null);
	}

	/**
	 * 返回成功消息
	 *
	 * @param msg  返回内容
	 * @param data 数据对象
	 * @return 成功消息
	 */
	public static <T> ResponseBase<T> success(String msg, T data) {
		return new ResponseBase<>(HttpStatus.HTTP_OK, msg, data);
	}

	/**
	 * 返回错误消息
	 *
	 * @return
	 */
	public static ResponseBase<Void> error() {
		return ResponseBase.error("操作失败");
	}

	/**
	 * 返回错误消息
	 *
	 * @param msg 返回内容
	 * @return 警告消息
	 */
	public static ResponseBase<Void> error(String msg) {
		return ResponseBase.error(msg, null);
	}

	/**
	 * 返回错误消息
	 *
	 * @param msg  返回内容
	 * @param data 数据对象
	 * @return 警告消息
	 */
	public static <T> ResponseBase<T> error(String msg, T data) {
		return new ResponseBase<>(HttpStatus.HTTP_INTERNAL_ERROR, msg, data);
	}

	/**
	 * 返回错误消息
	 *
	 * @param code 状态码
	 * @param msg  返回内容
	 * @return 警告消息
	 */
	public static ResponseBase<Void> error(int code, String msg) {
		return new ResponseBase<>(code, msg, null);
	}

}
