package cn.appsys.dao.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {

	List<AppInfo> getAppInfoList(@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,
			@Param(value="categoryLevel1")Integer queryCategoryLevel1,
			@Param(value="categoryLevel2")Integer queryCategoryLevel2,
			@Param(value="categoryLevel3")Integer queryCategoryLevel3,
			@Param(value="flatformId")Integer queryFlatformId,
			@Param(value="devId")Integer devId,
			@Param(value="from")Integer currentPageNo,
			@Param(value="pageSize")Integer pageSize);

	  
    int getAppInfoCount(@Param(value="softwareName")String querySoftwareName,
			   @Param(value="status")Integer queryStatus,
			   @Param(value="categoryLevel1")Integer queryCategoryLevel1,
			   @Param(value="categoryLevel2")Integer queryCategoryLevel2,
			   @Param(value="categoryLevel3")Integer queryCategoryLevel3,
			   @Param(value="flatformId")Integer queryFlatformId,
			   @Param(value="devId")Integer devId);
    
       int modify(AppInfo appInfo);
       
       //判断APKname是否唯一
       AppInfo getAppInfo(@Param(value="id")Integer id,@Param(value="APKName")String APKName);
       //更新app状态
       int updateSatus(@Param(value="status")Integer status,@Param(value="id")Integer id);
       //新增app
       int add(AppInfo appInfo);
       //根据appId，更新最新versionId
       int updateVersionId(@Param(value="versionId")Integer versionId,@Param(value="id")Integer appId);
       //删除
       int deleteAppLogo(@Param(value="id")Integer id);   
       //删除
      int deleteAppInfoById(@Param(value="id")Integer delId);
	
}
