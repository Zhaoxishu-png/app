package cn.appsys.dao.devuser;

import cn.appsys.pojo.DevUser;

public interface DevUserMapper {
	
	DevUser login(String devCode);

}
