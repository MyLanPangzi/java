package com.hiscat.hive.udaf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hiscat
 */
public class MeanResolver extends AbstractGenericUDAFResolver {
    public static final Logger LOGGER = LoggerFactory.getLogger(MeanResolver.class);

    @Override
    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
        return new MeanUDAFEvaluator();
    }

    static class MeanUDAFEvaluator extends GenericUDAFEvaluator {

        private transient Object[] partialResult;
        // For PARTIAL1 and COMPLETE
        private PrimitiveObjectInspector inputOI1;
        private PrimitiveObjectInspector inputOI2;
        // For PARTIAL2 and FINAL
        private transient StructObjectInspector soi;
        private transient StructField a;
        private transient StructField b;
        private ListObjectInspector aoi;
        private ListObjectInspector boi;

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);

            // init input
            if (m == Mode.PARTIAL1 || m == Mode.COMPLETE) {
                assert (parameters.length == 2);
                inputOI1 = (PrimitiveObjectInspector) parameters[0];
                inputOI2 = (PrimitiveObjectInspector) parameters[1];
            } else {
                assert (parameters.length == 1);
                soi = (StructObjectInspector) parameters[0];

                a = soi.getStructFieldRef("a");
                b = soi.getStructFieldRef("b");
                aoi = (ListObjectInspector) a.getFieldObjectInspector();
                boi = (ListObjectInspector) b.getFieldObjectInspector();
            }

            // init output
            if (m == Mode.PARTIAL1 || m == Mode.PARTIAL2) {
                // The output of a partial aggregation is a struct containing
                // a long count, two double averages, and a double covariance.

                ArrayList<ObjectInspector> foi = new ArrayList<ObjectInspector>();

                foi.add(ObjectInspectorFactory.getStandardListObjectInspector(aoi));
                foi.add(ObjectInspectorFactory.getStandardListObjectInspector(boi));

                ArrayList<String> fname = new ArrayList<String>();
                fname.add("a");
                fname.add("b");

                partialResult = new Object[2];
                partialResult[0] = new ArrayList<Double>();
                partialResult[1] = new ArrayList<Double>();

                return ObjectInspectorFactory.getStandardStructObjectInspector(fname, foi);

            } else {
                return PrimitiveObjectInspectorFactory.writableDoubleObjectInspector;
            }
        }

        @Override
        public AbstractAggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new MeanAgg();
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            MeanAgg meanAgg = (MeanAgg) agg;
            meanAgg.a = new ArrayList<>();
            meanAgg.b = new ArrayList<>();
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            MeanAgg meanAgg = (MeanAgg) agg;
            meanAgg.a.add(PrimitiveObjectInspectorUtils.getDouble(parameters[0], inputOI1));
            meanAgg.a.add(PrimitiveObjectInspectorUtils.getDouble(parameters[1], inputOI2));
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            MeanAgg meanAgg = (MeanAgg) agg;
            ((List<Double>) partialResult[0]).addAll(meanAgg.a);
            ((List<Double>) partialResult[1]).addAll(meanAgg.b);
            return partialResult;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            Object structFieldData = soi.getStructFieldData(partial, a);
            Object structFieldData2 = soi.getStructFieldData(partial, b);

        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            MeanAgg r = (MeanAgg) agg;
            return r.a.stream().mapToDouble(i -> i).sum() + r.b.stream().mapToDouble(i -> i).sum();
        }

        static class MeanAgg extends AbstractAggregationBuffer {
            ArrayList<Double> a;
            ArrayList<Double> b;

            @Override
            public int estimate() {
                return 64;
            }
        }
    }
}
