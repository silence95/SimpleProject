package com.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class AsmTester {
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassFormatError, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Class asmClass = generateAsmClass(Account.class,"$EnhancedByASM");
        Method method = asmClass.newInstance().getClass().getMethod("operate", new Class[]{});
        method.invoke(asmClass.newInstance(), new Object[]{});
    }
    
    public static Class generateAsmClass(Class clazz, String suffix) throws InstantiationException, IllegalAccessException, ClassFormatError, ClassNotFoundException {
        String fullName = clazz.getName();
        String packageName = fullName.substring(0,fullName.lastIndexOf("."));
        String enhancedName = clazz.getSimpleName() + suffix;
        
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

            AsmClassLoader asmClassLoader = new AsmClassLoader(AsmTester.class.getClassLoader());
            Class asmClass = asmClassLoader.defineClassFromClassFile(packageName + "." + enhancedName, classData);
            return asmClass;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static class AsmClassLoader extends ClassLoader {
        
        private byte[] classData;
        
        public AsmClassLoader(ClassLoader parent) {
            super(parent);
        }
        
        public Class defineClassFromClassFile(String className, byte[] classData ) throws ClassFormatError, ClassNotFoundException { 
            this.classData = classData;
            return defineClass(className, classData, 0, classData.length);
        }
    }
}
