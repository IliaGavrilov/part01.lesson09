import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {

    /* Кастомный класс лоадер */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if ("SomeClass".equals(name)) {
            return findClass(name);
        }
        return super.loadClass(name); // механизм загрузки
    }

    /* Кастомный поисковик и определитель класса */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("findClass starts work: " + name);
        if ("SomeClass".equals(name)) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get("SomeClass.class"));
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.findClass(name);
    }
}
