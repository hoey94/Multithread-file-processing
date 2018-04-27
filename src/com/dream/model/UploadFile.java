package com.dream.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@SuppressWarnings("all")
@Table(name = "uploadfile")
public class UploadFile extends AllId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312688589468135053L;

	private String fileName;

	private String uploadUserName;

	private String path;

	private String fileType;

	private String uuidFileName;

	private String fileSize;

	private System_User user;

	public String inputUserName;
	
	public String inputUserId;
	
	private String temp1;

	private String temp2;

	private String temp3;

	@ManyToOne(cascade = CascadeType.PERSIST,optional = true)
	@JoinColumn
	public System_User getUser() {
		return user;
	}

	public void setUser(System_User user) {
		this.user = user;
	}

	public String getFileName() {
		return fileName;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getInputUserId() {
		return inputUserId;
	}

	public void setInputUserId(String inputUserId) {
		this.inputUserId = inputUserId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}



	public String getUuidFileName() {
		return uuidFileName;
	}

	public void setUuidFileName(String uuidFileName) {
		this.uuidFileName = uuidFileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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
