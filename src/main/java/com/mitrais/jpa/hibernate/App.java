package com.mitrais.jpa.hibernate;

import com.mitrais.jpa.hibernate.entity.Account;
import com.mitrais.jpa.hibernate.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
        EntityManager em = emf.createEntityManager();


        saveObject(em, new Employee(101, "Che", "Guevara"));
        saveObject(em, new Employee(102, "Anthony", "Russo"));
        saveObject(em, new Employee(103, "Marc", "Joaquin"));

        saveObject(em, new Account(1, "Esa Rijal", 0.0));
        saveObject(em, new Account(2, "Rijal M", 100.0));

        Account account = em.find(Account.class, 2);
        System.out.println(account.getOwnerName());

        account.setOwnerName("Mustaqbal");
        saveObject(em, account);

        Account account2 = em.find(Account.class, 2);
        System.out.println(account2.getOwnerName());

        deleteObject(em, account2);
        account2 = em.find(Account.class, 2);
        assert account2 == null;

        // JPQL
        List resultList = em.createQuery("SELECT e FROM Employee e WHERE e.firstName LIKE :name")
                .setParameter("name", "Che").getResultList();
        System.out.println("Size of result: " + resultList.size());
        resultList.forEach((result) -> System.out.println(((Employee) result).getLastName()));

        // Criteria Query, advantages, ketahuan error di awal
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        cq.select(employee);
        cq.where(cb.equal(employee.get("firstName"), "Marc"));
        TypedQuery<Employee> query = em.createQuery(cq);
        List<Employee> results = query.getResultList();
        results.forEach((result) -> System.out.println(result.getLastName()));

    }



    private static void deleteObject(EntityManager em, Account account2) {
        em.getTransaction().begin();
        em.remove(account2);
        em.getTransaction().commit();
    }

    private static void saveObject(EntityManager em, Object object){
        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
    }


}
