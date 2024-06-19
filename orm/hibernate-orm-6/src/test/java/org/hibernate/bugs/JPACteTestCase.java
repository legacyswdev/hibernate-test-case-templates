package org.hibernate.bugs;

import jakarta.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of JPA/JQL common table expression (CTE)
 * https://in.relation.to/2023/02/20/hibernate-orm-62-ctes/
 *
 * @author legacyswdev
 */
public class JPACteTestCase  {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    @Test
    public void testCteJoinClause() {
        entityManagerFactory.createEntityManager().createQuery( "with c1 as (select c.id as id from CteEntity c), " +
                        "c2 as (select c.id as id from CteEntity c)" +
                        "  select c from CteEntity c" +
                        " join c1 c1_join on c.id = c1_join.id " +
                        " join c2 c2_join on c.id = c2_join.id" )
                .getResultList();
    }

    @Test
    public void testCteJoinCross() {
        entityManagerFactory.createEntityManager().createQuery( "select c1 from CteEntity c1,(select c.id as id from CteEntity c) c2," +
                        "       (select c.id as id from CteEntity c) c3" +
                        " where c1.id = c2.id and c2.id = c3.id" )
                .getResultList();
    }

    @Entity(name = "CteEntity")
    public static class CteEntity{
        @Id
        long id;
    }
}
