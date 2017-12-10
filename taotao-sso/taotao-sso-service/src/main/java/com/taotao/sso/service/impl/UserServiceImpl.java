package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	

	@Override
	public TaotaoResult checkData(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (1 == type) {// 1: username
			criteria.andUsernameEqualTo(param);
		} else if (2 == type) {// 2. cellphone
			criteria.andPhoneEqualTo(param);
		} else if (3 == type) {// 3. email
			criteria.andEmailEqualTo(param);
		} else {
			return TaotaoResult.build(400, "参数中包含非法数据");
		}

		List<TbUser> list = userMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return TaotaoResult.ok(false);
		}

		// 数据可用
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser tbUser) {
		// 检查数据的有效性
		if (StringUtils.isBlank(tbUser.getUsername())) {
			return TaotaoResult.build(400, "用户名不能为空");
		}
		// 判断用户名是否重复
		TaotaoResult result = checkData(tbUser.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "用户名重复");
		}
		// 判断密码为空
		if (StringUtils.isBlank(tbUser.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空");
		}

		if (StringUtils.isNotBlank(tbUser.getPhone())) {
			// 重复校验
			result = checkData(tbUser.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "电话号码重复");
			}
		}

		if (StringUtils.isNotBlank(tbUser.getEmail())) {
			// 重复校验
			result = checkData(tbUser.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "邮箱重复");
			}
		}

		// 补全pojo的属性
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		// 密码MD5加密
		String password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(password);

		// 插入数据
		userMapper.insert(tbUser);

		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		//判断用户名, 密码是否正确
		//密码要进行MD5加密再校验
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(tbUserExample);
		if(CollectionUtils.isEmpty(list)) {
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		
		//生成token, 使用UUID
		String token = UUID.randomUUID().toString();
		user.setPassword(null);// 清空密码
		
		//把用户信息保存到redis, key 是  token, value 是 用户信息
		jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
		//设置key的过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		
		//返回登录成功, 其中要把 token返回.
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(USER_SESSION + ":" + token);
		if(StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户登录已经过期");
		}
		
		//重置session的过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		
		//把json转换成User对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		jedisClient.del(token);
		return TaotaoResult.ok();
	}

}
