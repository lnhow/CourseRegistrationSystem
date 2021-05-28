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
    private static final boolean IS_DEBUG = false;
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();

            if (IS_DEBUG) {
                printMappedClass();
            }
        } catch (Throwable ex) {
            System.err.println(
                    "Initial SessionFactory creation failed." + ex);
            DialogUtil.showErrorMessage("Lỗi không kết nối được với CSDL\n"+ex.getMessage());
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
        }
    }
}