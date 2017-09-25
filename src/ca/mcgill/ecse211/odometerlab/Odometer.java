package ca.mcgill.ecse211.odometerlab;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer extends Thread {
  // robot position
  private double x;
  private double y;
  private double theta;
  private int leftMotorTachoCount;
  private int rightMotorTachoCount;
  private int nextLeftMotorTachoCount;
  private int nextRightMotorTachoCount;
  private EV3LargeRegulatedMotor leftMotor;
  private EV3LargeRegulatedMotor rightMotor;
  public static final double WHEEL_RADIUS = 2.1;  //DOUBLE CHECK THIS
  public static final double TRACK = 15.5;			//DOUBLE CHECK THIS

  private static final long ODOMETER_PERIOD = 25; /*odometer update period, in ms*/

  private Object lock; /*lock object for mutual exclusion*/

  // default constructor
  public Odometer(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
    this.x = -15.24;
    this.y = -15.24;
    this.theta = 0.0;
    this.leftMotorTachoCount = 0;
    this.rightMotorTachoCount = 0;
    lock = new Object();
  }

  // run method (required for Thread)
  public void run() {
    long updateStart, updateEnd;

    while (true) {
      updateStart = System.currentTimeMillis();
      // TODO put (some of) your odometer code here
      
      double DistL = 0;
      double DistR = 0;
      double deltaD = 0;
      double deltaT = 0;
      double dX = 0;
      double dY = 0;

      nextLeftMotorTachoCount = leftMotor.getTachoCount();
      nextRightMotorTachoCount= rightMotor.getTachoCount();
      
      DistL = WHEEL_RADIUS*Math.PI*(nextLeftMotorTachoCount - leftMotorTachoCount)/180;
      DistR = WHEEL_RADIUS*Math.PI*(nextRightMotorTachoCount - rightMotorTachoCount)/180;
      
      this.setLeftMotorTachoCount(nextLeftMotorTachoCount);
      this.setRightMotorTachoCount(nextRightMotorTachoCount);
      
      deltaD = (0.5)*(DistL + DistR);
      deltaT = (DistL - DistR)/(TRACK);
      
      
      

      synchronized (lock) {

    	 /**
         * Don't use the variables x, y, or theta anywhere but here! Only update the values of x, y,
         * and theta in this block. Do not perform complex math
         * 
         */
        //theta = 0.0000; // TODO replace example value
        
    	  
    	  
    	this.setTheta(((this.getTheta()*(Math.PI/180)) + deltaT)*(180/Math.PI));		//double check this
    	if((this.getTheta()*(Math.PI/180)) >= 2*Math.PI){
    		
    		this.setTheta(((this.getTheta()*(Math.PI/180)) - 2*Math.PI)*(180/Math.PI)) ;
    	}
        
    	if(this.getTheta() <= -2*Math.PI){
    		this.setTheta(((this.getTheta()*(Math.PI/180)) + 2*Math.PI)*(180/Math.PI));
    	}
        
        
        
        dX = deltaD * Math.sin(this.getTheta()*(Math.PI/180));
        dY = deltaD * Math.cos(this.getTheta()*(Math.PI/180));
        
        this.setX(this.getX() + dX);
        this.setY(this.getY() + dY);
        
        
        
        
        
      }
      
      
      

      // this ensures that the odometer only runs once every period
      updateEnd = System.currentTimeMillis();
      if (updateEnd - updateStart < ODOMETER_PERIOD) {
        try {
          Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
        } catch (InterruptedException e) {
          // there is nothing to be done here because it is not
          // expected that the odometer will be interrupted by
          // another thread
        }
      }
    }
  }

  public void getPosition(double[] position, boolean[] update) {
    // ensure that the values don't change while the odometer is running
    synchronized (lock) {
      if (update[0])
        position[0] = x;
      if (update[1])
        position[1] = y;
      if (update[2])
        position[2] = theta;
    }
  }

  public double getX() {
    double result;

    synchronized (lock) {
      result = x;
    }

    return result;
  }

  public double getY() {
    double result;

    synchronized (lock) {
      result = y;
    }

    return result;
  }

  public double getTheta() {
    double result;

    synchronized (lock) {
      result = theta;
    }

    return result;
  }

  // mutators
  public void setPosition(double[] position, boolean[] update) {
    // ensure that the values don't change while the odometer is running
    synchronized (lock) {
      if (update[0])
        x = position[0];
      if (update[1])
        y = position[1];
      if (update[2])
        theta = position[2];
    }
  }

  public void setX(double x) {
    synchronized (lock) {
      this.x = x;
    }
  }

  public void setY(double y) {
    synchronized (lock) {
      this.y = y;
    }
  }

  public void setTheta(double theta) {
    synchronized (lock) {
      this.theta = theta;
    }
  }

  /**
   * @return the leftMotorTachoCount
   */
  public int getLeftMotorTachoCount() {
    return leftMotorTachoCount;
  }

  /**
   * @param leftMotorTachoCount the leftMotorTachoCount to set
   */
  public void setLeftMotorTachoCount(int leftMotorTachoCount) {
    synchronized (lock) {
      this.leftMotorTachoCount = leftMotorTachoCount;
    }
  }

  /**
   * @return the rightMotorTachoCount
   */
  public int getRightMotorTachoCount() {
    return rightMotorTachoCount;
  }

  /**
   * @param rightMotorTachoCount the rightMotorTachoCount to set
   */
  public void setRightMotorTachoCount(int rightMotorTachoCount) {
    synchronized (lock) {
      this.rightMotorTachoCount = rightMotorTachoCount;
    }
  }
}
