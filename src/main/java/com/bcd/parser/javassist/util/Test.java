package com.bcd.parser.javassist.util;

import com.bcd.parser.impl.gb32960.data.Packet;
import com.bcd.parser.javassist.JavassistParser;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import io.netty.buffer.ByteBuf;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Test implements JavassistParser<Packet> {

    @Override
    public Packet parse(ByteBuf data, FieldProcessContext context) {
        return null;
    }

    public static void main(String[] args) throws NotFoundException {
        final CtClass ctClass = ClassPool.getDefault().get(Test.class.getName());
        final CtMethod declaredMethod = ctClass.getDeclaredMethods()[0];
        System.out.println(ctClass);
    }
}
