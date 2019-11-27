package cn.appsys.service.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	
	//�޸İ汾
	boolean modify(AppVersion appVersion);
	//�鿴��ϸ��Ϣ
	List<AppVersion> getAppVersionList(@Param("appId")Integer appId);
	//�����汾��Ϣ
	boolean appsysadd(AppVersion appVersion);

}
