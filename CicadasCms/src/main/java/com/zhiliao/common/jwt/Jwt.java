package com.zhiliao.common.jwt;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.zhiliao.common.jwt.vo.PayloadVo;
import com.zhiliao.common.jwt.vo.ResultVo;

import java.util.Date;
import java.util.Map;

/**
 * Description:jwt
 *
 * @author Jin
 * @create 2017-04-05
 **/
public class Jwt {
	
    
    /**
     * 秘钥
     */
    private static final byte[] SECRET="z122NNNlasewqWWefffdmhjSDccsdsa9//*-".getBytes();
    
    /**
     * 初始化head部分的数据为
     * {
     * 		"alg":"HS256",
     * 		"type":"JWT"
     * }
     */
    private static final JWSHeader header=new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null);

	/**
	 *生成Token
	 * @param payload
	 * @return
	 */
	public static String createToken(PayloadVo payload) {
		String tokenString=null;
		// 创建一个 JWS object
		JWSObject jwsObject = new JWSObject(header, new Payload(JSON.toJSONString(payload)));
		try {
			// 将jwsObject 进行HMAC签名
			jwsObject.sign(new MACSigner(SECRET));
			tokenString=jwsObject.serialize();
		} catch (JOSEException e) {
			System.err.println("签名失败:" + e.getMessage());
			e.printStackTrace();
		}
		return tokenString;
	}


	/**
	 *生成Token
	 * @param  map
	 * @return
	 */
	public static String createToken(Map map) {
		String tokenString=null;
		// 创建一个 JWS object
		JWSObject jwsObject = new JWSObject(header, new Payload(JSON.toJSONString(map)));
		try {
			// 将jwsObject 进行HMAC签名
			jwsObject.sign(new MACSigner(SECRET));
			tokenString=jwsObject.serialize();
		} catch (JOSEException e) {
			System.err.println("签名失败:" + e.getMessage());
			e.printStackTrace();
		}
		return tokenString;
	}


	/**
     * 校验token是否合法，返回Map集合,集合中主要包含    state状态码   data鉴权成功后从token中提取的数据
     * 该方法在过滤器中调用，每次请求API时都校验
     * @param token
     * @return  Map<String, Object>
     */
	public static ResultVo validToken(String token) {
		ResultVo result = new ResultVo();
		try {

			JWSObject jwsObject = JWSObject.parse(token);
			Payload payload = jwsObject.getPayload();
			PayloadVo payloadVo = JSON.parseObject(String.valueOf(payload.toJSONObject()),PayloadVo.class);

			JWSVerifier verifier = new MACVerifier(SECRET);

			if (jwsObject.verify(verifier)) {

				// token校验成功（此时没有校验是否过期）
				result.setStatus(TokenState.VALID.toString());
				// 若payload包含ext字段，则校验是否过期
				if (payloadVo.getExt()!=0) {
					long curTime = new Date().getTime();
					// 过期了
					if (curTime > payloadVo.getExt()) {
						result.setStatus(TokenState.EXPIRED.toString());
					}
				}
				result.setPayloadVo(payloadVo);

			} else {
				// 校验失败
				result.setStatus(TokenState.INVALID.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(TokenState.INVALID.toString());
		}
		return result;
	}	
    
}
