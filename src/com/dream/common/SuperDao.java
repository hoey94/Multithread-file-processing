package com.dream.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("superDao")
@SuppressWarnings("all")
public class SuperDao<T> {

	@Autowired
	public SessionFactory sessionFactory;

	/**
	 * 增加一条数据
	 * 
	 * @param t
	 */
	public void addT(T t) {
		this.sessionFactory.getCurrentSession().save(t);
	}

	/**
	 * 更新一条数据
	 * 
	 * @param t
	 */
	public void updateT(T t) {
		this.sessionFactory.getCurrentSession().update(t);
	}

	/**
	 * 删除一条数据
	 * 
	 * @param t
	 */
	public void delete(T t) {
		this.sessionFactory.getCurrentSession().delete(t);
	}

	/**
	 * 加载一条数据
	 * 
	 * @param t
	 * @param id
	 */
	public T getT(T t, String id) {
		return (T) this.sessionFactory.getCurrentSession()
				.get(t.getClass(), id);
	}

	/**
	 * 加载一条数据
	 * 
	 * @param t
	 * @param id
	 */
	public T getT(T t, Integer id) {
		return (T) this.sessionFactory.getCurrentSession()
				.get(t.getClass(), id);
	}

	/**
	 * 加载一条件数
	 * 
	 * @param t
	 * @param id
	 */
	public T loadT(T t, String id) {
		return (T) this.sessionFactory.getCurrentSession().load(t.getClass(),
				id);
	}

	/**
	 * 加载一条件数
	 * 
	 * @param t
	 * @param id
	 */
	public T loadT(T t, Integer id) {
		return (T) this.sessionFactory.getCurrentSession().load(t.getClass(),
				id);
	}

	/**
	 * 分页查询数据
	 * 
	 * @param hql
	 *            hsql语句
	 * @param start
	 *            开始数
	 * @param limit
	 *            查多少条
	 * @return
	 */
	public List getList(String hql, int start, int limit, String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		} else {
			hql += "  order by t.createTime desc";
		}
		Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (start != 0) {
			q.setFirstResult(start);
		}
		if (limit != 0) {
			q.setMaxResults(limit);
		}
		return q.list();
	}

	/**
	 * 分页查询数据，有条件和参数
	 * 
	 * @param hql
	 *            hql语句
	 * @param obj
	 *            参数
	 * @param start
	 *            开始数
	 * @param limit
	 *            查多少条
	 * @return
	 */
	public List getList(String hql, Object[] obj, int start, int limit,
			String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		} else {
			hql += "  order by t.createTime desc";
		}
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		if (start != 0) {
			query.setFirstResult(start);
		}
		if (limit != 0) {
			query.setMaxResults(limit);
		}
		return query.list();
	}

	/**
	 * 分页查询数据，有条件和参数
	 * 
	 * @param hql
	 *            hql语句
	 * @param obj
	 *            参数
	 * @param start
	 *            开始数
	 * @param limit
	 *            查多少条
	 * @return
	 */
	public T getListOne(String hql, Object[] obj, String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		} else {
			hql += "  order by t.createTime desc";
		}
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql)
				.setFirstResult(0).setMaxResults(1);
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		List<T> list = query.list();
		if (null == list || list.size() <= 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * 分页查询数据总数
	 * 
	 * @param hql
	 * @param desc
	 * @return
	 */
	public int getListCount(String hql, String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		} else {
			hql += "  order by t.createTime desc";
		}
		return Integer.valueOf(this.sessionFactory.getCurrentSession()
				.createQuery(hql).uniqueResult().toString());

	};

	/**
	 * 分页查询数据，有条件和参数
	 * 
	 * @param hql
	 *            hql语句
	 * @param obj
	 *            参数
	 * @param start
	 *            开始数
	 * @param limit
	 *            查多少条
	 * @return
	 */
	public int getListCount(String hql, Object[] obj, String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		} else {
			hql += "  order by t.createTime desc";
		}
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		return Integer.parseInt(query.list().get(0).toString());
	}

	public int deleteT(String hql, Object[] obj) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (null != obj) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		return query.executeUpdate();
	}

	public int deleteTSql(String hql) {
		return this.sessionFactory.getCurrentSession().createSQLQuery(hql)
				.executeUpdate();

	}

	/**
	 * 更新
	 * 
	 * @param hql
	 * @param obj
	 * @return
	 */
	public int exectUpdate(String hql, Object[] obj) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		return query.executeUpdate();

	}

	/**
	 * 分页查询数据
	 * 
	 * @param hql
	 *            hsql语句
	 * @param start
	 *            开始数
	 * @param limit
	 *            查多少条
	 * @return
	 */
	public List getListSql(String sql, Object[] obj, int start, int limit,
			String desc) {
		if (null != desc && !"".equals(desc)) {
			sql += "  " + desc;
		}
		Query q = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				q.setParameter(i, obj[i]);
			}
		}
		if (start != 0) {
			q.setFirstResult(start);
		}
		if (limit != 0) {
			q.setMaxResults(limit);
		}
		return q.list();
	}

	/**
	 * sql 查询统计
	 * 
	 * @param hql
	 * @param obj
	 * @param desc
	 * @return
	 */
	public int getListCountSql(String hql, Object[] obj, String desc) {
		if (null != desc && !"".equals(desc)) {
			hql += "  " + desc;
		}
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(
				hql);
		if (null != obj) {

			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
		return Integer.parseInt(query.list().get(0).toString());
	}

}
