package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.AppCategoryService;
import cn.appsys.service.developer.AppInfoService;
import cn.appsys.service.developer.AppVersionService;
import cn.appsys.service.developer.DataDictionarySevice;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppController {

	private Logger logger = Logger.getLogger(AppController.class);
	@Resource
	private AppInfoService appInfoService;
	@Autowired
	private DataDictionarySevice dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;

	@RequestMapping("list")
	public String list(Model model, HttpSession session,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) String _queryStatus,
			@RequestParam(value = "queryCategoryLevel1", required = false) String _queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String _queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String _queryCategoryLevel3,
			@RequestParam(value = "queryFlatformId", required = false) String _queryFlatformId,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		Integer devId = ((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		// 页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		Integer currentPageNo = 1;
		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if (_queryStatus != null && !_queryStatus.equals("")) {
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if (_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")) {
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if (_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")) {
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if (_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")) {
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if (_queryFlatformId != null && !_queryFlatformId.equals("")) {
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		// 总数量（表）
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1,
					queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		int PageNo = (currentPageNo - 1) * pageSize;

		List<AppInfo> appInfoList = appInfoService.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1,
				queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, PageNo, pageSize);
		// 查出所有app状态
		List<DataDictionary> statusList = dataDictionaryService.getDataDictionary("APP_STATUS");
		// 查出app所属平台
		List<DataDictionary> flatFormList = dataDictionaryService.getDataDictionary("APP_FLATFORM");
		List<AppCategory> categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);// 列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;

		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);

		// 二级分类列表和三级分类列表---回显
		if (queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")) {
			categoryLevel2List = appCategoryService.getAppCategoryListByParentId(queryCategoryLevel1);
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if (queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")) {
			categoryLevel3List = appCategoryService.getAppCategoryListByParentId(queryCategoryLevel2);
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}

		return "developer/appinfolist";
	}

	public List<DataDictionary> getDataDictionaryList(String typeCode) {
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService.getDataDictionary(typeCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDictionaryList;
	}
	    // 根据parentId查询出相应的分类级别列表
		@RequestMapping(value = "/categorylevellist.json", method = RequestMethod.GET)
		@ResponseBody
		public List<AppCategory> getAppCategoryList(@RequestParam String pid) {
			logger.debug("getAppCategoryList pid ====我是null======== " + pid);
			List<AppCategory> categoryLevelList =null;
			if (pid.equals(""))
				pid = null;
				try {
					categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid==null?null:Integer.parseInt(pid));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return categoryLevelList;
		}

		// 根据typeCode查询出相应的数据字典列表
		@RequestMapping(value = "/datadictionarylist.json", method = RequestMethod.GET)
		@ResponseBody
		public List<DataDictionary> getDataDicList(@RequestParam String tcode) {
			logger.debug("getDataDicList tcode ============ " + tcode);
			return dataDictionaryService.getDataDictionary(tcode);
		}
	//增加app信息（跳转到新增appinfo页面）
	@RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
	public String add(@ModelAttribute("appInfo") AppInfo appInfo){
		return "developer/appinfoadd";
	}
	//保存新增appInfo（主表）的数据
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String addSave(AppInfo appInfo,HttpSession session,HttpServletRequest request,@RequestParam(value="a_logoPicPath",required = false)MultipartFile attach) {
		String logoPicPath =null;
		String logoLocPath =null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setStatus(1);
		try {
			if (appInfoService.add(appInfo)) {
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}
	
	
	//在新增信息的时候判断APKName是否唯一
	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkNameIsExit(@RequestParam String APKName){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(APKName)){
			resultMap.put("APKName", "empty");
		}else{
			AppInfo appInfo = null;
			try {
				appInfo = appInfoService.getAppInfo(null, APKName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != appInfo)
				resultMap.put("APKName", "exist");
			else
				resultMap.put("APKName", "noexist");
		}
		return JSONArray.toJSONString(resultMap);
	}
	//查看详细信息
	@RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model) {
		AppInfo appInfo=null;
		List<AppVersion> appVersionList =null;
		try {
			
			if(id!=null) {				
				appInfo=appInfoService.getAppInfo(Integer.parseInt(id),null);
		        appVersionList=appVersionService.getAppVersionList(Integer.parseInt(id));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appInfo);
	
		return "developer/appinfoview";
		
	}
	//新增版本
	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
	public String addVersion(@RequestParam(value ="id")String appId,
			      @RequestParam(value="error",required= false)String fileUploadError,
			        AppVersion appVersion,Model model){
		logger.debug("fileUploadError============> " + fileUploadError);
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		appVersion.setAppId(Integer.parseInt(appId));
		List<AppVersion> appVersionList = null;
		try {
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
			appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId),null)).getSoftwareName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appVersion);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionadd";
	    }
	
	  //保存新增appversion数据（子表）-上传该版本的apk包
	  @RequestMapping(value="addversionsave",method=RequestMethod.POST)
	  public String addVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
			  @RequestParam(value="a_downloadLink",required= false) MultipartFile attach ) {  
		    String downloadLink =  null;
			String apkLocPath = null;
			String apkFileName = null;
			if(!attach.isEmpty()){
				String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
				logger.info("uploadFile path: " + path);
				String oldFileName = attach.getOriginalFilename();//原文件名
				String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
				if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
					 String apkName = null;
					 try {
						apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
					 } catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					 }
					 if(apkName == null || "".equals(apkName)){
						 return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
								 +"&error=error1";
					 }
					 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
					 File targetFile = new File(path,apkFileName);
					 if(!targetFile.exists()){
						 targetFile.mkdirs();
					 }
					 try {
						attach.transferTo(targetFile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
								 +"&error=error2";
					} 
					downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
					apkLocPath = path+File.separator+apkFileName;
				}else{
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
							 +"&error=error3";
				}
			}
			appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
			appVersion.setCreationDate(new Date());
			appVersion.setDownloadLink(downloadLink);
			appVersion.setApkLocPath(apkLocPath);
			appVersion.setApkFileName(apkFileName);
			try {
				if(appVersionService.appsysadd(appVersion)){
					return "redirect:/dev/flatform/app/list";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
		}
	  //修改APP信息
	  @RequestMapping(value="appinfomodify",method=RequestMethod.GET)
	  public String  modifyAppInfo(@RequestParam("id") String id,
				@RequestParam(value="error",required= false)String fileUploadError,
				Model model) {
		     AppInfo appinfo=null;
		     logger.debug("modifyAppInfo --------- id:"+ id); 
		     if(null != fileUploadError && fileUploadError.equals("error1")){
					fileUploadError = Constants.FILEUPLOAD_ERROR_1;
				}else if(null != fileUploadError && fileUploadError.equals("error2")){
					fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
				}else if(null != fileUploadError && fileUploadError.equals("error3")){
					fileUploadError = Constants.FILEUPLOAD_ERROR_3;
				}else if(null != fileUploadError && fileUploadError.equals("error4")){
					fileUploadError = Constants.FILEUPLOAD_ERROR_4;
				}
				try {
					appinfo = appInfoService.getAppInfo(Integer.parseInt(id),null);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addAttribute(appinfo);
				model.addAttribute("fileUploadError",fileUploadError);
				return "developer/appinfomodify";
	      }
	  
	   
	
	}
	

