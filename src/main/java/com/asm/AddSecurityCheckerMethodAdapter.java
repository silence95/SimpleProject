package com.asm;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckerMethodAdapter extends MethodAdapter{

    public AddSecurityCheckerMethodAdapter(MethodVisitor mv) {
        super(mv);
    }

    public void visitCode() { 
        visitMethodInsn(Opcodes.INVOKESTATIC, "com/asm/SecurityChecker", "checkSecurity", "()V"); 
    } 
}
