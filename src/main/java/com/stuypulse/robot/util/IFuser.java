/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.util;

import com.stuypulse.stuylib.streams.IStream;
import com.stuypulse.stuylib.streams.filters.HighPassFilter;
import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

/**
 * TODO: add documentation
 *
 * @author Myles Pasetsky
 */
public class IFuser implements IStream {

    private final IStream setpoint; // low frequency setpoint reading
    private final IStream measurement; // high frequency measurement reading

    private final Number filterRC;

    private IFilter setpointFilter;
    private IFilter measurementFilter;

    private double targetMeasurement;

    /** */
    public IFuser(Number rc, IStream setpoint, IStream measurement) {
        this.setpoint = setpoint;
        this.measurement = measurement;

        filterRC = rc;

        initialize(); // just for dummies
    }

    /** */
    public void initialize() {
        setpointFilter = new LowPassFilter(filterRC);
        measurementFilter = new HighPassFilter(filterRC);

        targetMeasurement = setpoint.get() + measurement.get();
    }

    private double getSetpoint() {
        return setpointFilter.get(setpoint.get());
    }

    private double getMeasurement() {
        return measurementFilter.get(targetMeasurement - measurement.get());
    }

    /** */
    public double get() {
        return getSetpoint() + getMeasurement();
    }
}
