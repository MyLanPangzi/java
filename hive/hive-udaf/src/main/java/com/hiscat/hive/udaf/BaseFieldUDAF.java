package com.hiscat.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;

import static java.lang.Math.exp;
import static java.lang.Math.log;

public class BaseFieldUDAF extends AbstractGenericUDAFResolver {
    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {

        if (parameters.length != 2) { // 判断参数长度
            throw new UDFArgumentLengthException("Exactly one argument is expected, but " +
                    parameters.length + " was passed!");
        }
        //第一个参数
        ObjectInspector objectInspector01 = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[0]);

        //第二个参数
        ObjectInspector objectInspector02 = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[1]);

        // 是不是标准的java Object的primitive类型
        if (objectInspector01.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentTypeException(0, "Argument type must be PRIMARY. but " +
                    objectInspector01.getCategory().name() + " was passed!");
        }

        if (objectInspector02.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentTypeException(0, "Argument type must be PRIMARY. but " +
                    objectInspector02.getCategory().name() + " was passed!");
        }


        // 如果是标准的java Object的primitive类型，说明可以进行类型转换
        PrimitiveObjectInspector inputOI01 = (PrimitiveObjectInspector) objectInspector01;
        PrimitiveObjectInspector inputOI02 = (PrimitiveObjectInspector) objectInspector02;

        // 如果是标准的java Object的primitive类型,判断是不是string类型，因为参数只接受string类型
        if (inputOI01.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
            throw new UDFArgumentTypeException(0, "Argument type must be Strig, but " +
                    inputOI01.getPrimitiveCategory().name() + " was passed!");
        }

        if (inputOI02.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
            throw new UDFArgumentTypeException(0, "Argument type must be Strig, but " +
                    inputOI02.getPrimitiveCategory().name() + " was passed!");
        }

        return new PredictUDAF();
    }

    public static class PredictUDAF extends GenericUDAFEvaluator {

        PrimitiveObjectInspector IntegerIO;
        NumAgg result = new NumAgg();

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {

            return super.init(m, parameters);
        }

        //存储当前的 day 和 ret 的数据
        static class NumAgg extends AbstractAggregationBuffer {
            StringBuffer day = new StringBuffer();
            StringBuffer ret = new StringBuffer();
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new NumAgg();
        }

        @Override
        public void reset(AggregationBuffer aggregationBuffer) throws HiveException {
            NumAgg agg = (NumAgg) aggregationBuffer;
            agg.day = null;
            agg.ret = null;
        }

        private boolean warned = false;

        @Override
        public void iterate(AggregationBuffer aggregationBuffer, Object[] objects) throws HiveException {
            NumAgg myAgg = (NumAgg) aggregationBuffer;
            //map 过来 来一个数 放进来
            if (objects[0] != null) {
                myAgg.day.append(objects[0] + ",");
            }

            if (objects[1] != null) {
                myAgg.ret.append(objects[1] + ",");
            }
        }

        @Override
        public Object terminatePartial(AggregationBuffer aggregationBuffer) throws HiveException {
            NumAgg myAgg = (NumAgg) aggregationBuffer;
            result.day.append(myAgg.day + ",");
            result.ret.append(myAgg.ret + ",");
            return result;
        }

        @Override
        public void merge(AggregationBuffer aggregationBuffer, Object o) throws HiveException {
            //reduce阶段
            NumAgg myAgg1 = (NumAgg) aggregationBuffer;
            NumAgg myAgg2 = (NumAgg) o;
            myAgg1.day.append(myAgg2.day + ",");
            myAgg1.ret.append(myAgg2.ret + ",");
        }

        @Override
        public Object terminate(AggregationBuffer aggregationBuffer) throws HiveException {
            NumAgg myAgg = (NumAgg) aggregationBuffer;
            result = myAgg;

            String numbers = result.ret.toString();
            String number = numbers.endsWith(",") ? numbers.substring(0, numbers.length() - 1) : numbers;
            String[] a = number.split(",");
            Double[] ds = new Double[a.length];
            for (int i = 0; i < a.length; i++) {
                Double d = Double.valueOf(a[i]);
                ds[i] = d;
            }
            return getResult(ds);
        }
    }


    public static String getResult(Double[] nums) {
        StringBuffer sb = new StringBuffer();
        Double[] y = new Double[nums.length];
        for (int j = 0; j < nums.length; j++) {
            y[j] = Double.valueOf(j) + 1;
        }
        Double sumX = 0.0, sumX2 = 0.0, sumY = 0.0, sumXY = 0.0, a, b, A;
        for (int i = 0; i < nums.length; i++) {
            sumX = sumX + log(y[i]);
            sumX2 = sumX2 + log(y[i]) * log(y[i]);
            sumY = sumY + log(nums[i]);
            sumXY = sumXY + log(y[i]) * log(nums[i]);
        }

        b = (nums.length * sumXY - sumX * sumY) / (nums.length * sumX2 - sumX * sumX);
        A = (sumY - b * sumX) / nums.length;
        /* Transformation of A to a */
        a = exp(A);

        //Math.pow(x,3)
        for (int i = nums.length + 1; i < nums.length + 1 + 15; i++) {
            sb.append(a * Math.pow(i, b));
            sb.append(",");
        }
        return sb.toString();
    }
}