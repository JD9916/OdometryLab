/*
 * OdometryCorrection.java
 */
package ca.mcgill.ecse211.odometerlab;

import lejos.hardware.Sound;

public class OdometryCorrection extends Thread {
  private static final long CORRECTION_PERIOD = 10;
  private Odometer odometer;
  private ColorSensorPoller poller;
  private int colorValue;
  private int xCalibration;
  private int yCalibration;
  
  

  // constructor
  public OdometryCorrection(Odometer odometer, ColorSensorPoller ColorSensorPoller) {
    this.odometer = odometer;
    this.poller = ColorSensorPoller;
    
  }

  // run method (required for Thread)
  public void run() {
    long correctionStart, correctionEnd;
    xCalibration = 0;
    yCalibration = 0;
    
    while (true) {
      correctionStart = System.currentTimeMillis();
      
      colorValue = poller.getColor();
            
      
      
      
	      if ((SquareDriver.getTurns() % 2) == 1) { 
		      if(colorValue < 30){
		    	  Sound.beep();
		    	  
		    	  if(odometer.getX()>=0) {
			    	  if((odometer.getX() % 30.48) >= 15.24) {
			    		  odometer.setX(odometer.getX() + (30.48-(odometer.getX()%30.48)));
			    	  }
			    	  else {
			    		  odometer.setX(odometer.getX() - (odometer.getX()%30.48));
			    	  }
			      }
		    	  else {
		    		  if((Math.abs(odometer.getX()) % 30.48) > 15.24) {
			    		  odometer.setX((Math.abs(odometer.getX()) + (30.48-(Math.abs(odometer.getX())%30.48)))*(-1));
			    	  }
			    	  else {
			    		  odometer.setX((Math.abs(odometer.getX()) - (Math.abs(odometer.getX())%30.48))*(-1));
			    	  }
		    	  }
		      }
	      }
	      else {
	    	  if(colorValue < 30){
	    		  Sound.beep();
	    		  if(odometer.getY()>=0) {
	    			  
			    	  if((odometer.getY() % 30.48) >= 15.24) {
			    		  odometer.setY(odometer.getY() + (30.48-(odometer.getY()%30.48)));
			    	  }
			    	  else {
			    		  odometer.setY(odometer.getY() - (odometer.getY()%30.48));
			    	  }
	    		  }
	    		  else {
	    			  if((Math.abs(odometer.getY()) % 30.48) > 15.24) {
			    		  odometer.setY((Math.abs(odometer.getY()) + (30.48-(Math.abs(odometer.getY())%30.48)))*(-1));
			    	  }
			    	  else {
			    		  odometer.setY((Math.abs(odometer.getY()) - (Math.abs(odometer.getY())%30.48))*(-1));
			    	  }
	    		  }
		      }
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
