package model;

import exceptions.InvalidValueException;

import java.time.LocalDateTime;

import static java.lang.Math.pow;
import static java.lang.System.nanoTime;

public class PointHandler {

    private static final byte MAX_Y = 3;
    private static final byte MIN_Y = -3;
    private static final byte MAX_X = 4;
    private static final byte MIN_X = -4;


    public Hit getHitInfo(PointData pointData) {
        long start = nanoTime();

        validatePointData(pointData);
        boolean hit = isHit(pointData);
        long executionTime = nanoTime() - start;

        return Hit.builder().xVal(pointData.getPoint().getXVal())
                .yVal(pointData.getPoint().getYVal())
                .rVal(pointData.getRVal())
                .currentTime(LocalDateTime.now().minusMinutes(pointData.getTimezone()))
                .isHit(hit)
                .executionTime(executionTime / 1000)
                .build();
    }

    private void validatePointData(PointData point) {
        if (!(inRange(point.getPoint().getXVal(), MIN_X, MAX_X))) {
            throw new InvalidValueException("Значение X не попадает в нужный интервал!");
        }

        if (!(inRange(point.getPoint().getYVal(), MIN_Y, MAX_Y))) {
            throw new InvalidValueException("Значение Y не попадает в нужный интервал!");
        }

        if (point.getRVal() <= 0) {
            throw new InvalidValueException("Значение R не может быть неположительным!");
        }
    }

    private boolean inRange(double value,double min,double max) {
        return value >= min && value <= max;
    }

    private boolean isHit(PointData pointData) {
        return isRectangleHit(pointData) || isCircleHit(pointData) || isTriangleHit(pointData);
    }

    private boolean isRectangleHit(PointData pointData) {
        return pointData.getPoint().getXVal() <= 0 && pointData.getPoint().getXVal() >= -pointData.getRVal()
                && pointData.getPoint().getYVal() <= 0 && pointData.getPoint().getYVal() >= -pointData.getRVal();
    }

    private boolean isCircleHit(PointData pointData) {
        return pointData.getPoint().getXVal() >= 0 && pointData.getPoint().getYVal() >= 0
                && (pow(pointData.getPoint().getXVal(), 2) + pow(pointData.getPoint().getYVal(), 2)) <= pow(pointData.getRVal() / 2, 2);
    }

    private boolean isTriangleHit(PointData pointData) {
        return pointData.getPoint().getXVal() >= 0 && pointData.getPoint().getYVal() <= 0
                && (2 * pointData.getPoint().getXVal() - pointData.getRVal()) <= 2 * pointData.getPoint().getYVal();
    }

}
