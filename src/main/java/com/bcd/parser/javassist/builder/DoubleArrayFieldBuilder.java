package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class DoubleArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instance_var_name = context.instance_var_name;
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
                JavassistUtil.packetField_singleLen_notSupport(field);
            }
        }
        String arr_var_name = fieldVarName + "_arr";
        JavassistUtil.append(body,"double[] {}=new double[{}];\n",arr_var_name, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arr_var_name);

        String valExpr=null;
        switch (packetField.singleLen()) {
            case 2: {
                valExpr=JavassistUtil.format("(double){}.readUnsignedInt()",byteBuf_var_name);

                break;
            }
            case 4: {
                valExpr=JavassistUtil.format("(double){}.readLong()",byteBuf_var_name);
                break;
            }
        }
        JavassistUtil.append(body,"{}[i]={};\n",arr_var_name,JavassistUtil.replace_var_to_valExpr(packetField.valExpr(),valExpr));
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}({});\n", instance_var_name, setMethodName, arr_var_name);
    }
}
