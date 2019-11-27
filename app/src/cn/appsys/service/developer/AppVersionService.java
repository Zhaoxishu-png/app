package cn.appsys.service.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	
	//修改版本
	boolean modify(AppVersion appVersion);
	//查看详细信息
	List<AppVersion> getAppVersionList(@Param("appId")Integer appId);
	//新增版本信息
	boolean appsysadd(AppVersion appVersion);

}
