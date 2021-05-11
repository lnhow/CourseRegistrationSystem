package com.lnh.CourseRegistration.Utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            printMappedClass();
        } catch (Throwable ex) {
            System.err.println(
                    "Initial SessionFactory creation failed." + ex);
            System.err.println(ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void printMappedClass() {
        MetamodelImplementor metaModelImpl = (MetamodelImplementor)sessionFactory.getMetamodel();
        Map<String, EntityPersister> entityPersisters = metaModelImpl.entityPersisters();
        Collection<EntityPersister> val = entityPersisters.values();

        System.out.println("Number of mapped class: " + val.size());
        for (EntityPersister ep : val) {
            AbstractEntityPersister aep = (AbstractEntityPersister)ep;

            System.out.println(aep.getTableName());
            // System.out.println(Arrays.toString(aep.getIdentifierColumnNames()));
//            for (String propName : aep.getPropertyNames()) {
//                System.out.println(propName);
//                System.out.println(Arrays.toString(aep.getPropertyColumnNames(propName)));
//            }
        }
    }
}