package Utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Фабрика Hibernate
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = configureSessionFactory();

    /**
     * Создание фабрики Hibernate
     * @return {@link SessionFactory}
     */
    private static SessionFactory configureSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")//Конфигурация из файла
                .build();
        System.out.println("Создание фабрики");
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    /**
     * Получение фабрики сессий
     * @return {@link SessionFactory}
     */
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
