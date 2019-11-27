package cn.appsys.service.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	
	//����������ѯ��app�б�
	List<AppInfo> getAppInfoList(String querySoftwareName,Integer queryStatus,
			Integer queryCategoryLevel1,Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,Integer queryFlatformId,
			Integer devId,Integer currentPageNo,Integer pageSize);
	
	//����������ѯappInfo���¼��
	int getAppInfoCount(@Param(value="softwareName")String querySoftwareName,
			   @Param(value="status")Integer queryStatus,
			   @Param(value="categoryLevel1")Integer queryCategoryLevel1,
			   @Param(value="categoryLevel2")Integer queryCategoryLevel2,
			   @Param(value="categoryLevel3")Integer queryCategoryLevel3,
			   @Param(value="flatformId")Integer queryFlatformId,
			   @Param(value="devId")Integer devId);
	
	 boolean modify(AppInfo appInfo);
    
     AppInfo getAppInfo(@Param(value="id")Integer id,@Param(value="APKName")String APKName);
     //����app
     boolean add(AppInfo appInfo);
     //update Sale Status By AppId and Operator
     boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj);
}
