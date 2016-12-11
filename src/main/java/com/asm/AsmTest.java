package com.asm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class AsmTest {
    
    private static AsmClassLoader asmClassLoader = new AsmClassLoader(AsmTest.class.getClassLoader());
  
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Account account = new Account();
        Account account1 = generateAsmClass(account);
//        Account.class.getMethod(name, parameterTypes).invoke(account, new Object[]{});
//        account.getClass().newInstance().operate();
//        account1.operate();
        ((Account)account.getClass().newInstance()).operate();
//        ((clazz.getClass())secureAccountClass.newInstance()).operate();
    }
    
    public static <T>T generateAsmClass(T t) throws InstantiationException, IllegalAccessException {
        String fullName = t.getClass().getName();
        String packageName = fullName.substring(0,fullName.lastIndexOf("."));
        String enhancedName = t.getClass().getSimpleName();
        
        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            AddSecurityCheckerClassAdapter classAdapter = new AddSecurityCheckerClassAdapter(classWriter);
            classAdapter.setEnhancedName(packageName.replace(".", "/") + "/"+ enhancedName);
            ClassReader classReader = new ClassReader(fullName);
            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);
            
            byte[] classData = classWriter.toByteArray();
            File file = new File("target/classes/" + packageName.replace(".", "/") + "/" + enhancedName + ".class");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(classData);
            fos.close();
            
            Class asmClass = asmClassLoader.defineClassFromClassFile(packageName + "." + enhancedName, file.getAbsolutePath());
//            Class asmClass = asmClassLoader.loadClass(packageName + "." + enhancedName);
            return (T)asmClass.newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static class AsmClassLoader extends ClassLoader {
        
        private byte[] classData;
        private String url = "";
        
        public AsmClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class loadClass(String className) {
            try {
                URL myUrl = new URL("file:" + url);
                URLConnection connection = myUrl.openConnection();
                InputStream input = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int data = input.read();

                while(data != -1){
                    buffer.write(data);
                    data = input.read();
                }

                input.close();

                byte[] classData = buffer.toByteArray();

                return defineClass(className,
                        classData, 0, classData.length);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
//        , byte[] classData
        public Class defineClassFromClassFile(String className, String url ) throws ClassFormatError { 
            this.classData = classData;
            this.url = url;
            return loadClass(className);
        } 
        
        
    }
}
