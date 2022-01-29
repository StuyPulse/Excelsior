package com.stuypulse.robot.sl;

import com.revrobotics.RelativeEncoder;

/**
 * Wrapper for ann encoder built into a NEO motor or, in code, 
 * a RelativeEncoder. 
 * 
 * Handles position from shifting gears (e.g. changing position factor
 * while the encoders are being used) by accumulating position as this
 * factor is changed.
 * 
 * @author Myles Pasetsky
 */
public class SLNEOEncoder implements SLEncoder {

    private final RelativeEncoder encoder;
    private double position;

    public SLNEOEncoder(RelativeEncoder encoder) {
        this.encoder = encoder;
        reset();
    }

    @Override
    public double getPosition() {
        return position + encoder.getPosition();
    }

    @Override
    public void setPosition(double position) {
        position = 0;
        encoder.setPosition(position);
    }

    @Override
    public void setConversionFactor(double factor) {
        position += encoder.getPosition();
        encoder.setPosition(0);
        encoder.setPositionConversionFactor(factor);   
    }

    @Override
    public double getConversionFactor() {
        return encoder.getPositionConversionFactor();
    }

    @Override
    public boolean isReversed() {
        return encoder.getInverted();
    }

    @Override
    public void setReversed(boolean reversed) {
        encoder.setInverted(reversed);
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }

}