package groupId.artifactId.config;

import groupId.artifactId.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppContext {
    private final AnnotationConfigApplicationContext context;
    private volatile static AppContext instance = null;

    public AppContext() {
        this.context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    public static AnnotationConfigApplicationContext getContext() {
        synchronized (AppContext.class) {
            if (instance == null) {
                instance = new AppContext();
            }
        }
        return instance.context;
    }
}
