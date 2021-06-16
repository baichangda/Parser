package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析long[]类型字段
 */
public class LongArrayProcessor extends FieldProcessor<long[]> {
    public final static int BYTE_LENGTH=8;

    @Override
    public long[] process(ByteBuf data, FieldProcessContext processContext){
        int len =processContext.getLen();
        if(len==0){
            return new long[0];
        }
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        //优化处理 int->long
        switch (singleLen){
            case 4:{
                long[] res=new long[len/4];
                for(int i=0;i<res.length;i++){
                    long cur=data.readUnsignedInt();
                    //验证异常、无效值
                    if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(cur,singleLen)){
                        res[i]=cur;
                    }else {
                        res[i] = (long) RpnUtil.calc_0(valExpr, res[i]);
                    }
                }
                return res;
            }
            case BYTE_LENGTH:{
                long[] res=new long[len/BYTE_LENGTH];
                for(int i=0;i<res.length;i++){
                    long cur=data.readLong();
                    //验证异常、无效值
                    if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(cur,singleLen)){
                        res[i]=cur;
                    }else {
                        res[i] = (long) RpnUtil.calc_0(valExpr, res[i]);
                    }
                }
                return res;
            }
            default:{
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        }

    }

    @Override
    public void deProcess(long[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int len = data.length;
        if(len ==0){
            return;
        }
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        long[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new long[len];
            for(int i = 0; i< len; i++){
                if(ParserUtil.checkInvalidOrExceptionVal_long(data[i],singleLen)){
                    newData[i]=(long) RpnUtil.deCalc_0(valExpr,data[i]);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        switch (singleLen){
            case 4:{
                for (long num : newData) {
                    dest.writeInt((int)num);
                }
                return;
            }
            case BYTE_LENGTH:{
                for (long num : newData) {
                    dest.writeLong(num);
                }
                return;
            }
            default:{
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        }

    }

}
