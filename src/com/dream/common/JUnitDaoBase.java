package com.dream.common;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * <b>Summary: </b> TODO Junit 基础类,加载环境 <b>Remarks: </b> TODO DAO测试基础类
 */
// @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/conf/beans.xml" })
public class JUnitDaoBase extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private SessionFactory sessionFactory;

	// @Test
	// @Transactional(propagation=Propagation.REQUIRED)
	// public void testAddUser() {
	// Sys_User su = new Sys_User();
	// // TODO Auto-generated method stub
	// sessionFactory.getCurrentSession().save(su);
	// System.out.println("1111");
	//
	// }

}
