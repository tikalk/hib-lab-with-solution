package com.tikal.hibernate.lab.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.LazyInitializationException;
import org.junit.Test;

import com.tikal.hibernate.lab.model.Address;
import com.tikal.hibernate.lab.model.Car;
import com.tikal.hibernate.lab.model.Celebrity;
import com.tikal.hibernate.lab.model.Person;


public class JpaTest {
	
	private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hib-lab-jpa-hsql");


	@Test
	public void testByCriteria(){
		final Person person = createPerson(new Person("zzz"));
		
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();	
			final Person p = runCriteriaQuery(em);
			assertEquals(person.getName(),p.getName());
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
	}

	private Person runCriteriaQuery(final EntityManager em) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Person> q = cb.createQuery(Person.class);
		final Root<Person> p = q.from(Person.class);
		q.select(p).where(cb.equal(p.get("name"), "zzz"));
		return em.createQuery(q).getSingleResult();		
	}

	// test the saving a person
	@Test
	public void createPerson() {
		createPerson(new Person("aaa"));
	}

	private Person createPerson(final Person person) {
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;		
		try {
			tx = em.getTransaction();
			tx.begin();			
			em.persist(person);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		assertNotNull(person.getId());
		return person;
	}
	
	private Car createCar(final Car car) {
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;		
		try {
			tx = em.getTransaction();
			tx.begin();			
			em.persist(car);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		assertNotNull(car.getId());
		return car;
	}
	
	private Person findPerson(final int id) {
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		Person p = null;
		try {
			tx = em.getTransaction();
			tx.begin();			
			p = em.find(Person.class, id);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		return p;
	}

	@Test
	public void updatePerson() {
		final Person person = createPerson(new Person("bbb"));
		
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		person.setAge(66);
		try {
			tx = em.getTransaction();
			tx.begin();
			em.merge(person);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		
		assertEquals(66,findPerson(person.getId()).getAge());
	}

	@Test
	public void testDeletePerson() {
		Person person = createPerson(new Person("ccc"));
		assertNotNull(findPerson(person.getId()));
		
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		person.setAge(66);
		try {
			tx = em.getTransaction();
			tx.begin();
			person = em.merge(person);
			em.remove(person);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		
		assertNull(findPerson(person.getId()));
	}

	@Test
	public void testGetPerson() {
		final Person person = createPerson(new Person("ddd"));
		assertEquals("ddd", findPerson(person.getId()).getName());
	}

	@Test
	public void testAgeQuery() {
		createPerson(new Person("eee",55));
		createPerson(new Person("fff",66));
		
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			@SuppressWarnings("unchecked")
			final
			List<Person> loadedPersons = em.createQuery("select p from Person p where p.age > :ageParam").setParameter("ageParam", 30).getResultList();
			assertTrue(loadedPersons.size() >= 2);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
	}

	@Test
	public void testCelebrityAndAddress(){
		final Celebrity celeb = new Celebrity("celeb");
		celeb.setAddress(new Address(5,"myStreet","myCity"));
		final String famousName = "very famous name";
		celeb.setFamousName(famousName);
		createPerson(celeb);
		
		final Person loaded = findPerson(celeb.getId());
		assertEquals(famousName, ((Celebrity)loaded).getFamousName());
	}

	@Test
	public void testCreatePersonWithCar() {
		final Car car = createCar(new Car("Volvo"));		
		final Person person = createPerson(new Person("ggg"));
		
		person.addCar(car);
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			em.merge(person);
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		
		em = emf.createEntityManager();
		tx = null;
		Person p = null;
		try {
			tx = em.getTransaction();
			tx.begin();			
			p = em.find(Person.class, person.getId());
			assertTrue(!p.getCars().isEmpty());
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}
		
	}

	@Test
	public void testLazyInitialization() {
		final Person person = createPerson(new Person("hhh"));
		final Person loaded = findPerson(person.getId());
		try{
			loaded.getCars().size();
			fail("should have failed here");
		} catch (final LazyInitializationException e) {
		}
	}

	@Test
	public void testQueryPersonWithCar() {
		final Car car = createCar(new Car("BMW"));	
		Person person = new Person("iii");
		person.addCar(car);
		person = createPerson(person);
		
		
		final EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			@SuppressWarnings("unchecked")
			final
			List<Person> loadedPersons = em
					.createQuery("select p from Person p left join fetch p.cars car where car.serialNo = :serialNo")
					.setParameter("serialNo", "BMW").getResultList();
			assertTrue(!loadedPersons.isEmpty());
			tx.commit();
		} catch (final RuntimeException e) {
			if(tx!=null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			  em.close();
		}

	}
}
