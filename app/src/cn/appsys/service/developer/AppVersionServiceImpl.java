package cn.appsys.service.developer;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;

@Service
public class AppVersionServiceImpl implements AppVersionService {
	
	@Resource
	private  AppVersionMapper mapper;
	
	@Resource
	private AppInfoMapper appInfoMapper;

	@Override
	public boolean modify(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(mapper.modify(appVersion) > 0){
			flag = true;
		}
		return flag;
	}

	@Override
	public List<AppVersion> getAppVersionList(Integer appId) {
		// TODO Auto-generated method stub
		return mapper.getAppVersionList(appId);
	}

	@Override
	public boolean appsysadd(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Integer versionId = null;
		if(mapper.add(appVersion) > 0){
			versionId = appVersion.getId();
			flag = true;
		}
		if(appInfoMapper.updateVersionId(versionId, appVersion.getAppId()) > 0 && flag){
			flag = true;
		}
		return flag;
	}

}
