package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.TicketDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.ITicketDao;

import java.beans.PropertyVetoException;

public class TicketDaoSingleton {
    private final ITicketDao ticketDao;
    private volatile static TicketDaoSingleton instance = null;

    public TicketDaoSingleton() {
        try {
            this.ticketDao = new TicketDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for TicketDao", e);
        }
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
