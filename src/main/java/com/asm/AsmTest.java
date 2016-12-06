package com.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class AsmTest {
    
    private static AsmClassLoader asmClassLoader = new AsmClassLoader();
  
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Account account = new Account();
        try {
            ClassReader classReader = new ClassReader("com.asm.Account");
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassAdapter classAdapter = new AddSecurityCheckerClassAdapter(classWriter);
            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);
            byte[] classData = classWriter.toByteArray();
            File file = new File("target/classes/com/asm/Account$EnhancedByASM.class");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(classData);
            fos.close();
            Class secureAccountClass = asmClassLoader.defineClassFromClassFile("Account$EnhancedByASM", classData);
            ((Account)secureAccountClass.newInstance()).operate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static class AsmClassLoader extends ClassLoader {
        public Class defineClassFromClassFile(String className, byte[] classFile) throws ClassFormatError { 
            return defineClass("com.asm.Account$EnhancedByASM", classFile, 0, classFile.length);
        } 
    }
}
