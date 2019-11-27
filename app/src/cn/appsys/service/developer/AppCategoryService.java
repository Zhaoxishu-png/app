package cn.appsys.service.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	
	List<AppCategory> getAppCategoryListByParentId(@Param("parentId")Integer parentId);
    
}
