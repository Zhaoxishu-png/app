package cn.appsys.service.developer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.devuser.DevUserMapper;
import cn.appsys.pojo.DevUser;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
	
	@Resource
	private DevUserMapper mapper;

	@Override
	public DevUser login(String devCode) {
		// TODO Auto-generated method stub
		return mapper.login(devCode);
	}

}
