package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class DoubleArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        String lenRes=context.lenRes;
        switch (packetField.singleLen()) {
            case 4: {
                lenRes = "(" + lenRes + ")/4";
                break;
            }
            case 8: {
                lenRes = "(" + lenRes + ")/8";
                break;
            }
            default: {
                JavassistUtil.packetFieldSingleLenNotSupport(field);
            }
        }
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body,"double[] {}=new double[{}];\n",arrVarName, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arrVarName);

        String valExpr=null;
        switch (packetField.singleLen()) {
            case 2: {
                valExpr=JavassistUtil.format("(double){}.readUnsignedInt()", FieldBuilder.varNameByteBuf);

                break;
            }
            case 4: {
                valExpr=JavassistUtil.format("(double){}.readLong()", FieldBuilder.varNameByteBuf);
                break;
            }
        }
        JavassistUtil.append(body,"{}[i]={};\n",arrVarName,JavassistUtil.replaceVarToValExpr(packetField.valExpr(),valExpr));
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}({});\n", varNameInstance, setMethodName, arrVarName);
    }
}
