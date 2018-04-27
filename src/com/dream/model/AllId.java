package com.dream.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class AllId implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public String table_id;
	public Long createTime;
	public String showTime;
	public String desc = "desc";// asc
	public String orderby;
	public int start = 0;
	public int limit = 20;
	public int page = 0;
	public int rows = 20;
	// 这下面的字段是用来将来联合查询的时候，创建合并实体
	private String string1;
	private String string2;
	private String string3;
	private String string4;
 
	private Long long1;
	private Long long2;
	private Long long3;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@GeneratedValue(generator = "system-uuid")
	public String getTable_id() {
		return table_id;
	}

	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {

		this.createTime = createTime;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {

		this.showTime = showTime;
	}

	@Transient
	public int getStart() {
		if (start > 0) {
			return (start - 1) * this.limit;
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	@Transient
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Transient
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public AllId(String table_id) {
		super();
		this.table_id = table_id;
	}

	public AllId() {
		super();
	}

	@Transient
	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}

	@Transient
	public String getString2() {
		return string2;
	}

	public void setString2(String string2) {
		this.string2 = string2;
	}

	@Transient
	public String getString3() {
		return string3;
	}

	public void setString3(String string3) {
		this.string3 = string3;
	}

	@Transient
	public Long getLong1() {
		return long1;
	}

	public void setLong1(Long long1) {
		this.long1 = long1;
	}

	@Transient
	public Long getLong2() {
		return long2;
	}

	public void setLong2(Long long2) {
		this.long2 = long2;
	}

	@Transient
	public Long getLong3() {
		return long3;
	}

	public void setLong3(Long long3) {
		this.long3 = long3;
	}

	@Transient
	public String getString4() {
		return string4;
	}

	public void setString4(String string4) {
		this.string4 = string4;
	}

	@Transient
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Transient
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	 

}
