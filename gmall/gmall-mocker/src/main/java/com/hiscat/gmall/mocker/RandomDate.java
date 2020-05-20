package com.hiscat.gmall.mocker;

import java.util.Date;
import java.util.Random;

/**
 * @author Administrator
 */
public class RandomDate {

    Long logDateTime;
    int maxTimeStep;

    public RandomDate(Date startDate, Date endDate, int num) {
        long avgStepTime = (endDate.getTime() - startDate.getTime()) / num;
        this.maxTimeStep = (int) avgStepTime * 2;
        this.logDateTime = startDate.getTime();
    }

    public Date getRandomDate() {
        int timeStep = new Random().nextInt(maxTimeStep);
        logDateTime = logDateTime + timeStep;
        return new Date(logDateTime);
    }
}
