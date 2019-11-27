package cn.appsys.service.developer;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appcategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;

@Service
public class AppCategoryServiceImpl implements AppCategoryService {
	
	@Resource
	private AppCategoryMapper mapper;

	@Override
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId) {
		return mapper.getAppCategoryListByParentId(parentId);
	}
	
	

}
