package com.dream.common;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("contextUtil")
public class ContextUtil implements ApplicationContextAware {
	private static ApplicationContext context;

	@Autowired
	public static SessionFactory sessionFactory;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static SessionFactory getSessionFactory() {

		return (SessionFactory) context.getBean("sessionFactory");
	}

	public static void setSessionFactory(SessionFactory sessionFactory1) {
		sessionFactory = sessionFactory1;
	}

}
