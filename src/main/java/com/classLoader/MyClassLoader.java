package com.classLoader;

import java.io.*;
import java.util.jar.JarFile;

/**
 * implements class loader by developer.
 * 一般情况下我们应该重写findClass的方法，而不是loadClass的方法。
 * findClass 是符合“父类委托”机制的，一般情况下loadClass先委托给parent去加载，如果父亲找不到才会调用子类的findClass方法。
 * ClassLoader.loadClass源码里面存在一个递归调用的过程
 */
public class MyClassLoader extends ClassLoader {

  private String jarPath;

  MyClassLoader(ClassLoader classLoader) {
    super(classLoader);
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] classData = loadClassData(name);
    if (classData == null) {
      throw new ClassNotFoundException();
    } else {
      return defineClass(name, classData, 0, classData.length);
    }
  }

  private byte[] loadClassData(String className) {
    try {
      System.out.println("start load" + className + "class data.");
      JarFile file = new JarFile(jarPath);
      byte[] result;
      String jvmClassName = className.replace('.', '/') + ".class";

      try (InputStream ins = file.getInputStream(file.getJarEntry(jvmClassName))) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length = 0;
        while ((length = ins.read(buffer)) != -1) {
          baos.write(buffer, 0, length);
        }
        result = baos.toByteArray();
        baos.close();
        ins.close();
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setJarPath(String jarPath) {
    this.jarPath = jarPath;
  }

  public static void main(String[] args) {
    MyClassLoader classLoader =
      new MyClassLoader(Thread.currentThread().getContextClassLoader());
    classLoader.setJarPath("D:/workspace/SimpleProject-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
    int count = 5;
    while (count-- > 0) {
      try {
        String className = "com.classLoader.TestClazz";
        Class<?> testClass = classLoader.loadClass(className);
        Object testClassObj = testClass.newInstance();
        ITestClazz testClazz = (ITestClazz) testClassObj;
        testClazz.run();

        System.out.println("classLoader:" + testClassObj.getClass().getClassLoader());
        System.out.println("is ITestClazz:" + (testClassObj instanceof ITestClazz));
        System.out.println("MyClassLoader can find TestClazz:" + classLoader.findLoadedClass(className));


        MyClassLoader classLoader2 = new MyClassLoader(classLoader);
        System.out.println("MyClassLoader2 can find TestClazz:" + classLoader2.findLoadedClass(className));

        System.gc();
        Thread.sleep(10000);
        System.gc();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
