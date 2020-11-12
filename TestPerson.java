/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.person.model;

import com.person.model.Person;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.util.Assert;

/**
 *
 * @author Muzamiru.Mbaziira
 */
public class TestPerson {

    private static EntityManagerFactory emf;
    private EntityManager em;

    public TestPerson() {
    }

    @BeforeAll
    public static void createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("Person-Information");
    }

    @AfterAll
    public static void closeEntityManagerFactory() {
        emf.close();
    }

    @BeforeEach
    public void beginTransaction() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void validating_fullname_and_Id() {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Person person = new Person();
        em.persist(person);
        try {
            em.getTransaction().commit();

            fail("ConstraintViolationException exptected.");

        } catch (RollbackException e) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e.getCause()).getConstraintViolations();
            for (ConstraintViolation v : violations) {
                System.out.print("The constraint is " + v);   //log.info(v);
            }
        }

        em.close();
    }

}
