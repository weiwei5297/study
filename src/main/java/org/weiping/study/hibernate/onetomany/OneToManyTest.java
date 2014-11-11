package org.weiping.study.hibernate.onetomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.weiping.study.hibernate.HibernateUtil;

/**
 * @author Weiping Ye
 * @version 创建时间：2014-4-4 下午2:37:24
 **/
public class OneToManyTest {

	public static void main(String[] args) {
		OneToManyTest o2m = new OneToManyTest();
//		o2m.testSave();
		o2m.testFind();
	}
	
	private void testFind() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();

		Department d = (Department) session.get(Department.class, 115L);
		System.out.println(d);
		System.out.println(d.getEmployees());
		
		session.getTransaction().commit();
		session.close();
	}
	
	private void testSave() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();

		Department department = new Department();
		department.setDepartmentName("Sales");
		session.save(department);

		Employee emp1 = new Employee("Nina", "Mayers", "111");
		Employee emp2 = new Employee("Tony", "Almeida", "222");

		emp1.setDepartment(department);
		emp2.setDepartment(department);

		session.save(emp1);
		session.save(emp2);

		session.getTransaction().commit();
		session.close();
	}

}
