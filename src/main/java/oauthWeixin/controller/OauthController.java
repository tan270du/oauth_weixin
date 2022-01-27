package oauthWeixin.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oauthWeixin.base.BaseApiService;
import oauthWeixin.utils.HttpClientUtils;
import oauthWeixin.utils.WeiXinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Api(tags = "登录模块Controller")
@Controller
public class OauthController extends BaseApiService {
	final static Logger logger = LoggerFactory.getLogger(OauthController.class);

	@Autowired
	private WeiXinUtils weiXinUtils;

	// 生成授权链接
	@GetMapping("/authorizedUrl")
	@ApiOperation("授权url")
	public String authorizedUrl() throws UnsupportedEncodingException {
		logger.info("请求授权url");
		return "redirect:" + weiXinUtils.getAuthorizedUrl();
	}

	// 微信授权回调地址
	@PostMapping("/callback")
	@ApiOperation("微信返回地址")
	public boolean callback(String code, HttpServletRequest request) {
		// 1.使用Code 获取 access_token
		String accessTokenUrl = weiXinUtils.getAccessTokenUrl(code);
		JSONObject resultAccessToken = HttpClientUtils.httpGet(accessTokenUrl);
		boolean containsKey = resultAccessToken.containsKey("errcode");

		if (containsKey) {
			request.setAttribute("errorMsg", "系统错误!");
			String errorPage = "errorPage";
			return false;
		}
		// 2.使用access_token获取用户信息
		String accessToken = resultAccessToken.getString("access_token");
		String openid = resultAccessToken.getString("openid");
		// 3.拉取用户信息(需scope为 snsapi_userinfo)
		String userInfoUrl = weiXinUtils.getUserInfo(accessToken, openid);
		JSONObject userInfoResult = HttpClientUtils.httpGet(userInfoUrl);
		System.out.println("userInfoResult:" + userInfoResult);
		request.setAttribute("nickname", userInfoResult.getString("nickname"));
		request.setAttribute("city", userInfoResult.getString("city"));
		request.setAttribute("headimgurl", userInfoResult.getString("headimgurl"));
		return true;
	}

}
