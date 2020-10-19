package jvm;

import java.io.InputStream;
import java.lang.reflect.Method;

public class XlassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Class<?> cls = new XlassLoader().findClass("Hello");
            Method hello = cls.getDeclaredMethod("hello");

            hello.invoke(cls.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("name = " + name);

        InputStream is = XlassLoader.class.getResourceAsStream(name + ".xlass");

        if (is == null)
            return super.findClass(name);

        try {
            byte[] bytes = new byte[is.available()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - is.read());
            }

            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
}