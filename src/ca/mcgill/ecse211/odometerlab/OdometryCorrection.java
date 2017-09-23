/*
 * OdometryCorrection.java
 */
package ca.mcgill.ecse211.odometerlab;

public class OdometryCorrection extends Thread {
  private static final long CORRECTION_PERIOD = 10;
  private Odometer odometer;
  private ColorSensorPoller poller;
  private int colorValue;
  

  // constructor
  public OdometryCorrection(Odometer odometer, ColorSensorPoller ColorSensorPoller) {
    this.odometer = odometer;
    this.poller = ColorSensorPoller;
    
  }

  // run method (required for Thread)
  public void run() {
    long correctionStart, correctionEnd;

    while (true) {
      correctionStart = System.currentTimeMillis();
      
      odometer.getX();
      colorValue = poller.getColor();
      
      //System.out.println("Color Value" + colorValue);
      if(colorValue < 35){
    	  odometer.setX(0);
      }
      
      
      //TODO Place correction implementation here
      

      // this ensure the odometry correction occurs only once every period
      correctionEnd = System.currentTimeMillis();
      if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
        try {
          Thread.sleep(CORRECTION_PERIOD - (correctionEnd - correctionStart));
        } catch (InterruptedException e) {
          // there is nothing to be done here because it is not
          // expected that the odometry correction will be
          // interrupted by another thread
        }
      }
    }
  }
}
