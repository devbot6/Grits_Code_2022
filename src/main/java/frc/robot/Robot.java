// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import java.sql.Time;

import javax.xml.stream.events.StartElement;

// import java.io.PushbackInputStream;

// import javax.swing.ButtonGroup;
// import javax.swing.plaf.basic.BasicToggleButtonUI;
// import javax.xml.stream.events.StartElement;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
//import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.WaitCommand;

//this is the 1st change of grits code using github


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final PWMSparkMax m_leftDrive = new PWMSparkMax(0);
  private final PWMSparkMax m_rightDrive = new PWMSparkMax(1);
  private final PWMSparkMax m_shooter = new PWMSparkMax(2);
  private final PWMSparkMax m_elevator = new PWMSparkMax(3);
  //private final PWMSparkMax m_rightLift = new PWMSparkMax(4);
  //private final PWMSparkMax m_leftLift = new PWMSparkMax(4);
  private final PWMSparkMax m_intake = new PWMSparkMax(5);
  private final PWMSparkMax m_climb = new PWMSparkMax(6);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive, m_rightDrive);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_Stick2 = new Joystick(1);
  private final Timer m_timer = new Timer();



 
  

  boolean toggleOn = false;
  boolean togglePressed = false;
  boolean initShoot = false;
  
  




  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightDrive.setInverted(true);
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {//initializes timer for autonmous period
    m_timer.reset();
    m_timer.start();
    initShoot = true;
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {//code that will run during the autonmous period
    // Drive for 2 seconds
    if (m_timer.get() < .5) {
      m_robotDrive.arcadeDrive(-0.7, 0.0);
  }
      // drive forwards half speed
    else {
      m_robotDrive.stopMotor();
       // stop robot

    }
    shoot(initShoot, 0, 0.6, -0.7);
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during teleoperated mode. */
  int currentTime;
  int startTimer;
  boolean testBool;
 
  @Override
  public void teleopPeriodic(){

   
   //elevator
   if(m_stick.getRawButton(2) == true){

    m_elevator.set(.5);

   }
   else{
     m_elevator.set(0);
   }

  
  //Arcade Drive
  double speed = .7;
  double xSpeed = m_Stick2.getRawAxis(1) * speed;
  double ZRotation = m_Stick2.getRawAxis(2) * speed;
  m_robotDrive.arcadeDrive(-xSpeed, ZRotation);
  currentTime = (int)m_timer.get();
  

      
  // //reset time button
  // if(m_stick.getRawButton(6)){
  //   m_timer.reset();
  //   System.out.println(m_timer.get());
  //   System.out.println("timer reset");
  // }

  
  //Intake
    if (m_stick.getRawButton(1) == true){
      System.out.println("INTAKE");
        m_intake.set(-.7);
    }
  else{
    m_intake.set(.0);
    }



  //Traversal up & down
  

  if(m_stick.getRawButton(5)){
    System.out.println("climb go up");
    m_climb.set(.6);
  }
  else if(m_stick.getRawButton(6)){
    System.out.println("climb go down");
    m_climb.set(-.6);
  }
  else{
    m_climb.set(0);
  }

  //shooter test
  if(m_stick.getRawButton(4)){
    System.out.println("shoot");
    m_shooter.set(.7);
  }
  else{
    m_shooter.set(0);
  }
  
  

  
  // //elevator & shooter & shooter spin up
   if(m_stick.getRawButton(3)){
     initShoot = true;
     m_timer.reset();
     m_timer.start();
   }

     shoot(initShoot, -0.4, 0.6, -0.7);
  
}

















public void shoot(boolean run, double downElevator, double upElevator, double shooterValue){

  if(run == true){
  testBool = true;



  if(m_timer.get()>0 && m_timer.get()<.5 && testBool == true){
    System.out.println(m_timer.get());
    m_elevator.set(downElevator);
   System.out.println("elevator moves down an inch");
   
  }
  else if(m_timer.get()>1 && m_timer.get()<3 && testBool == true){
    System.out.println(m_timer.get());
    m_shooter.set(shooterValue);
    System.out.println("shooter spins");
    System.out.println("2 second delya");
    if(m_timer.get()>2 && m_timer.get()<3 && testBool == true){
      System.out.println(m_timer.get());
      m_elevator.set(upElevator);
      System.out.println("elevator goes back up for 2 seconds");
  }
  }
  else{
    System.out.println(m_timer.get());
    System.out.println("everything stops");
    m_shooter.set(0);
    m_elevator.set(0);
  }
}

}






  


  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
