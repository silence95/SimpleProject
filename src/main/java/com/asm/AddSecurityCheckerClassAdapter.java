package com.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class AddSecurityCheckerClassAdapter extends ClassAdapter{

    private String enhancedSuperName;
    private String enhancedName;
    
    public AddSecurityCheckerClassAdapter(ClassVisitor cv) {
        super(cv);
    }
    
    public void visit(final int version, final int access, final String name, 
            final String signature, final String superName, 
            final String[] interfaces) { 
        enhancedSuperName = name; // 改变父类，这里是”Account”
        super.visit(version, access, enhancedName, signature, enhancedSuperName, interfaces); 
    }

    public MethodVisitor visitMethod(final int access, final String name, 
            final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodVisitor mvWrapper = mv;
        if(mv != null) {
            mvWrapper = new AddSecurityCheckerMethodAdapter(mv);
            if (name.equals("<init>")) { 
                mvWrapper = new ChangeToChildConstructorMethodAdapter(mv, enhancedSuperName); 
            } 
        }
        return mvWrapper;
        
    }
    
    public String getEnhancedName() {
        return enhancedName;
    }

    public void setEnhancedName(String enhancedName) {
        this.enhancedName = enhancedName;
    }
    
}
