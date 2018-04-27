package com.dream.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

@SuppressWarnings("all")
@Entity
@Table(name = "system_user")
public class System_User extends AllId {

	public String userName;// 账号

	public String userPwd;// 密码

	public String useremail;// 邮箱

	private List<UploadFile> listUploadFile = new ArrayList<UploadFile>();
	
	private String temp1;
	
	private String temp2;
	
	private String temp3;

	@OneToMany(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "user")
	public List<UploadFile> getListUploadFile() {
		return listUploadFile;
	}

	public void setListUploadFile(List<UploadFile> listUploadFile) {
		this.listUploadFile = listUploadFile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getTemp3() {
		return temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}
	
	
}
