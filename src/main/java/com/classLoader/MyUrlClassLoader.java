package com.classLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * jar如果最外层存在其他文件夹，也作为url的一部分。
 * 如bootRepackage打包的url一般为jar:file:/D:/workspace/SimpleProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar!/BOOT-INF/classes/
 */
public class MyUrlClassLoader extends URLClassLoader {

  public MyUrlClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public static void main(String[] args)
    throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    URL url = new URL("jar:file:/D:/workspace/SimpleProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar!/");
    MyUrlClassLoader classLoader = new MyUrlClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());


    Class<?> testClass = classLoader.loadClass("com.classLoader.TestClazz");
    Object testClassObj = testClass.newInstance();
    ITestClazz testClazz = (ITestClazz) testClassObj;
    testClazz.run();

    System.out.println("classLoader:" + testClassObj.getClass().getClassLoader());
    System.out.println("is ITestClazz:" + (testClassObj instanceof ITestClazz));

  }
}
