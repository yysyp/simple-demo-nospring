package ps.demo.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

public class MyClassLoader extends URLClassLoader {

    public static final String MAIN = "main";

    public MyClassLoader(URL url) {
        super(new URL[]{url});
    }

    public MyClassLoader(URL[] urls) {
        super(urls);
    }

    public MyClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public String getMainClassName(URL mainJarUrl) throws IOException {
        URL u = new URL("jar", "", mainJarUrl + "!/");
        JarURLConnection uc = (JarURLConnection) u.openConnection();
        Attributes attr = uc.getMainAttributes();
        return attr != null ? attr.getValue(Attributes.Name.MAIN_CLASS) : null;
    }

    public void invokeMainClass(String name, String[] args) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = loadClass(name);
        Method m = c.getMethod(MAIN, new Class[]{args.getClass()});
        m.setAccessible(true);
        int mods = m.getModifiers();
        if (m.getReturnType() != void.class || !Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
            throw new NoSuchMethodException("main");
        }
        m.invoke(null, new Object[]{args});
    }


    public static void main(String[] args) throws Exception {
        File classFolder = new File("docs/");

        MyClassLoader cl = new MyClassLoader(classFolder.toURI().toURL());
//        cl.invokeMainClass("pip.WriteFileContentPip1", args);
        Class klass = cl.loadClass("pip.WriteFileContentPip1");
        Method handle = klass.getMethod("handle", new Class[] {File.class, String.class});
        String result = "" + handle.invoke(klass.newInstance(), new File("docs/todo-list.txt"), "test content");
        System.out.println(result);

    }

}

