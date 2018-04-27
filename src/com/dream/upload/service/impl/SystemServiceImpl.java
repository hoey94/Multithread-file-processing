package com.dream.upload.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Component;

import bigupload.FileUploadUtil;

import com.dream.common.DataInit;
import com.dream.common.SuperDao;
import com.dream.model.System_User;
import com.dream.model.UploadFile;
import com.dream.upload.service.SystemService;

@Component("systemServiceImpl")
@SuppressWarnings("all")
public class SystemServiceImpl extends SuperDao<Object> implements SystemService{

	@Override
	public boolean checkUser(System_User model) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from System_User t where t.userName = ? and t.userPwd = ? ";
		int count = this.getListCountSql(sql, new Object[]{model.getUserName() ,model.getUserPwd()}, "");
		if(count >= 0){
			return true;
		}
		return false;
	}

	@Override
	public void insertUploadFileInfo(UploadFile model) {
		// TODO Auto-generated method stub
		this.addT(model);
	}

	@Override
	public Map<String, Object> getUploadFileMap(UploadFile model) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		sb.append("from UploadFile t where 1 = 1 ");
		if(!StringUtils.isEmpty(model.getFileName())){
			sb.append(" and t.fileName like ? ");
			params.add("%"+model.getFileName()+"%");
		}
		if(!StringUtils.isEmpty(model.getInputUserId())){
			sb.append(" and t.inputUserId =  ? ");
			params.add(model.getInputUserId());
		}
		List<UploadFile> list = this.getList(sb.toString(),params.toArray(),model.getStart(),model.getLimit(), "");
		int sum = this.getListCount("select count(*) "+sb.toString(), params.toArray(), "");
		int count = (int) Math.ceil(Double.valueOf(sum)/Double.valueOf(model.getLimit()));
		
		for(UploadFile u:list){
			u.setTemp1(createDownHtml(u));
		}
		
		map.put("list", list);
		map.put("count", count);
		map.put("sum", list.size());
		
		return map;
	}

	@Override
	public System_User selectOne(System_User model) {
		// TODO Auto-generated method stub
		String sql = "from System_User t where t.userName = ? and t.userPwd = ? ";
		System_User u = (System_User) this.getListOne(sql, new Object[]{model.getUserName() ,model.getUserPwd()}, "");
		if(u == null || "".equals(u.getTable_id())){
			return null;
		}
		return u;
	}

	@Override
	public void deleteModel(UploadFile model) {
		// TODO Auto-generated method stub
		this.delete(this.getT(new UploadFile(), model.getTable_id()));
	}
	
	private String createDownHtml(UploadFile uploadFile){
		String baseUrl = FileUploadUtil.getDownloadbaseUrl() + uploadFile.getPath();
		StringBuffer sb = new StringBuffer();

		sb.append("<a style='cursor: pointer;' target='_blank' href= '"+baseUrl+"' >下载<a/> ");
		
		return sb.toString();
	}

	@Override
	public void insertSystemUser(System_User model) {
		// TODO Auto-generated method stub
		this.addT(model);
	}

	@Override
	public List<System_User> getUserInfo() {
		// TODO Auto-generated method stub
		String sql = "select DISTINCT(inputUserId) as table_id  ,uploadUserName as userName from uploadfile";
		List<System_User> list = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
										.addScalar("userName", StringType.INSTANCE)
										.addScalar("table_id",StringType.INSTANCE)
										.setResultTransformer(Transformers.aliasToBean(System_User.class)).list();
		return list;
	}
}
