package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.TicketDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.ITicketDao;

public class TicketDaoSingleton {
    private final ITicketDao ticketDao;
    private volatile static TicketDaoSingleton instance = null;

    public TicketDaoSingleton() {
        this.ticketDao = new TicketDao(EntityManagerFactoryHibernate.getEntityManager());
    }

    public static ITicketDao getInstance() {
        synchronized (TicketDaoSingleton.class) {
            if (instance == null) {
                instance = new TicketDaoSingleton();
            }
        }
        return instance.ticketDao;
    }
}
