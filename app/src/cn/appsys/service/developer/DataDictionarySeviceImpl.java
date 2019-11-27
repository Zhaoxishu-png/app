package cn.appsys.service.developer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.datadictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;

@Service
public class DataDictionarySeviceImpl implements DataDictionarySevice {
	
	@Autowired
	private DataDictionaryMapper mapper;

	@Override
	public List<DataDictionary> getDataDictionary(String typeCode) {
		// TODO Auto-generated method stub
		return mapper.getDataDictionary(typeCode);
	}

}
