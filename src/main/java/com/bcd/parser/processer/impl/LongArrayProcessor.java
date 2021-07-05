package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析long[]类型字段
 */
public class LongArrayProcessor extends FieldProcessor<long[]> {

    @Override
    public long[] process(ByteBuf data, FieldProcessContext processContext){
        int len =processContext.len;
        if(len==0){
            return new long[0];
        }
        int singleLen= processContext.fieldInfo.packetField_singleLen;
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        //优化处理 int->long
        if(singleLen==4){
            long[] res=new long[len>>>2];
            for(int i=0;i<res.length;i++){
                long cur=data.readUnsignedInt();
                //验证异常、无效值
                if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(cur,singleLen)){
                    res[i]=cur;
                }else {
                    res[i] = RpnUtil.calc_long(valExpr, res[i]);
                }
            }
            return res;
        }else if(singleLen==8){
            long[] res=new long[len>>>3];
            for(int i=0;i<res.length;i++){
                long cur=data.readLong();
                //验证异常、无效值
                if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(cur,singleLen)){
                    res[i]=cur;
                }else {
                    res[i] = RpnUtil.calc_long(valExpr, res[i]);
                }
            }
            return res;
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(long[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if(len ==0){
            return;
        }
        int singleLen= processContext.fieldInfo.packetField_singleLen;
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        long[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new long[len];
            for(int i = 0; i< len; i++){
                if(ParserUtil.checkInvalidOrExceptionVal_long(data[i],singleLen)){
                    newData[i]=RpnUtil.deCalc_long(valExpr,data[i]);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        if(singleLen==4){
            for (long num : newData) {
                dest.writeInt((int)num);
            }
        }else if(singleLen==8){
            for (long num : newData) {
                dest.writeLong(num);
            }
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }

    }

}
