package com.stuypulse.robot.sl;

import edu.wpi.first.wpilibj.Encoder;

/**
 * A wrapper for a quadtrature encoder that uses WPILib's
 * Encoder class to read the 2/3 digital inputs necessary 
 * for quadtrature encoders.
 * 
 * Implements a "setPosition" in software because WPILib's 
 * encoder class only provides a "reset"
 * 
 * @author Myles Pasetsky
 */
public class SLWPIEncoder implements SLEncoder{

    private final Encoder encoder;
    private double distance;
    // private boolean reversed;

    public SLWPIEncoder(Encoder encoder) {
        this.encoder = encoder;
        reset();
        // setReversed(false);
    }

    @Override
    public double getPosition() {
        return distance + encoder.getDistance();
    }

    @Override
    public double getVelocity() {
        return encoder.getRate();
    }

    @Override
    public void setPosition(double position) {
        distance = position;
        encoder.reset();
    }

    @Override
    public void setConversionFactor(double ratio) {
        encoder.setDistancePerPulse(ratio);
    }

    @Override
    public double getConversionFactor() {
        return encoder.getDistancePerPulse();
    }

    // // @Override
    // public boolean isReversed() {
    //     return reversed;
    // }

    @Override
    public void setReversed(boolean reversed) {
        // this.reversed = reversed;
        encoder.setReverseDirection(reversed);
    }

    public int getVelocitySamples() {
        return encoder.getSamplesToAverage();
    }

    public void setVelocitySamples(int samples){
        encoder.setSamplesToAverage(samples);
    }

}
