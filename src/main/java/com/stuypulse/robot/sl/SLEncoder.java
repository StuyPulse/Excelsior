package com.stuypulse.robot.sl;

/**
 * Interface for common encoder operations.
 * 
 * Especially important in the drivetrain, where this
 * abstraction will allow us to play with Grayhill encoders,
 * while being able to rely on built-in encoders.
 * 
 * @author Myles Pasetsky
 */
public interface SLEncoder {

    // TODO: returning SLEncoder from set methods may make it more convenient to use
    // ,but the types wouldn't match so it can't be listed like that in the interface

    // TODO: consider making this a subsystem? mostly for putting information on
    // smartdashboard, potentially for using periodic internally

    // TODO: consider having an IntegratorEncoder that is an SLEncoder and takes in
    // an SLEncoder so any encoder can use distance integration
    /*
     * default SLIntegratedEncoder withIntegration() {
     * return new SLIntegratedEncoder(this);
     * }
     */

    // TODO: consider removing set position, not really used outside of zeroing
    // (should use reset for that)

    // TODO: implementing interfaces could have more constructors

    // NOTE: the interface doesn't require providing a reference to the underlying encoder,
    // there are workarounds with generics but one can just store the implementing type instead
    // of the interface to workaround this

    // TODO: MAYBE add methods to guess if encoders are working

    double getPosition();
    double getVelocity();

    void setPosition(double position);
    default void reset() {
        setPosition(0);
    }

    void setConversionFactor(double ratio);
    double getConversionFactor();

    boolean isReversed();
    void setReversed(boolean reversed);
}
