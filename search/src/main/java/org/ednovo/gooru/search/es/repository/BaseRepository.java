package org.ednovo.gooru.search.es.repository;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class BaseRepository {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	/*@Resource(name = "sessionFactoryReadOnly")
	private SessionFactory sessionFactoryReadOnly;*/

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public SessionFactory setSessionFactory(SessionFactory sessionFactory) {
		return sessionFactory;
	}

/*	public SessionFactory getSessionFactoryReadOnly() {
		return sessionFactoryReadOnly;
	}*/

	@SuppressWarnings("unchecked")
	public static <T> List<T> list(Query query) {
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T[]> arrayList(Query query) {
		return query.list();
	}
}
