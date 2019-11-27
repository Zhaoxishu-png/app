package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	
	//�޸İ汾
	int modify(AppVersion appVersion);
	//�鿴��ϸ��Ϣ
	List<AppVersion> getAppVersionList(@Param("appId")Integer appId);
	//�����汾��Ϣ
	int add(AppVersion appVersion);

}
