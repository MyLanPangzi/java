package com.hiscat.gmall.mocker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
public class RandomOptionGroup<T> {

    int totalWeight = 0;

    List<RanOpt<T>> optList = new ArrayList<>();

    @SafeVarargs
    public RandomOptionGroup(RanOpt<T>... opts) {
        for (RanOpt<T> opt : opts) {
            totalWeight += opt.getWeight();
            for (int i = 0; i < opt.getWeight(); i++) {
                optList.add(opt);
            }
        }
    }

    public RanOpt<T> getRandomOpt() {
        int i = new Random().nextInt(totalWeight);
        return optList.get(i);
    }


    public static void main(String[] args) {
        RanOpt[] opts = {new RanOpt("zhang3", 20), new RanOpt("li4", 30), new RanOpt("wang5", 50)};
        RandomOptionGroup randomOptionGroup = new RandomOptionGroup(opts);
        for (int i = 0; i < 10; i++) {
            System.out.println(randomOptionGroup.getRandomOpt().getValue());
        }
    }

}