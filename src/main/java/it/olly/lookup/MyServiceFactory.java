package it.olly.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class MyServiceFactory {
    private static Map<String, MyServiceInterface> implementations = new HashMap<String, MyServiceInterface>();
    static {
        try (ScanResult scanResult = new ClassGraph().enableClassInfo()
                .acceptPackages("*")
                .scan()) {
            ClassInfoList classList = scanResult.getClassesImplementing(MyServiceInterface.class.getName());
            List<Class<?>> loadClasses = classList.loadClasses();
            for (Class<?> clazz : loadClasses) {
                MyServiceInterface impl = (MyServiceInterface) clazz.newInstance();
                implementations.put(impl.type(), impl);
            }
        } catch (Exception e) {
        }
    }

    public static final MyServiceInterface getService(String type) {
        return implementations.get(type);
    }
}
