package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	
	//修改版本
	int modify(AppVersion appVersion);
	//查看详细信息
	List<AppVersion> getAppVersionList(@Param("appId")Integer appId);
	//新增版本信息
	int add(AppVersion appVersion);

}
