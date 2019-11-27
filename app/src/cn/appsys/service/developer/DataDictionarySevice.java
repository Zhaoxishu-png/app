package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionarySevice {
	List<DataDictionary> getDataDictionary(String typeCode);
}
