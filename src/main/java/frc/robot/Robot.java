// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// cooper
package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

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
  private final PWMSparkMax m_arm = new PWMSparkMax(4);
  //private final PWMSparkMax m_rightLift = new PWMSparkMax(4);
  //private final PWMSparkMax m_leftLift = new PWMSparkMax(4);
  private final PWMSparkMax m_intake = new PWMSparkMax(5);
  //private final PWMSparkMax m_climb = new PWMSparkMax(6);
  private final DigitalInput limitSwitchRight = new DigitalInput(5);
  private final DigitalInput limitSwitchLeft = new DigitalInput(4);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive, m_rightDrive);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_Stick2 = new Joystick(1);
  private final Timer m_timer = new Timer();
  

  boolean toggleOn = false;
  boolean togglePressed = false;
  




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
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {//code that will run during the autonmous period
    // Drive for 2 seconds 
    if (m_timer.get() < 5.0) {
      m_robotDrive.arcadeDrive(0.0, 0.0);
      // drive forwards half speed
    } else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic(){
    
   

   //high shooter
   if(m_stick.getRawButton(6) == true){
    
    m_shooter.set(-.7);
    //System.out.println("is pressed");

   }
   else{
     m_shooter.set(0);;
    // System.out.println("is not pressed");
   }
   
  

   if(limitSwitchRight.get() == false){
    System.out.println("rightswitch");
   }
   
   
   if(limitSwitchLeft.get() == false){
    System.out.println("leftswitch");
   }
     
   

  
   //low shooter
   if(m_stick.getRawButton(5) == true){

    m_shooter.set(-.5);
    // System.out.println("is pressed");
   }
   else{
     m_shooter.set(0);
    //  System.out.println("is not pressed");
   }
   
   //elevator
   if(m_stick.getRawButton(2) == true){

    m_elevator.set(.5);

   }
   else{
     m_elevator.set(0);
   }

 
  
      double speed = .7;
      double xSpeed = m_Stick2.getRawAxis(1) * speed;
      double ZRotation = m_Stick2.getRawAxis(2) * speed;
      m_robotDrive.arcadeDrive(-xSpeed, ZRotation);

   //intake up
   if(limitSwitchRight.get()){
    if(m_Stick2.getRawButton(5) == true){

      m_arm.set(-1.0); 
    }

    else{
      m_arm.set(0);
    }
  }
  //intake down

   if(m_Stick2.getRawButton(6) == true){

     m_arm.set(0.5);
   }
   else{
     m_arm.set(0);
   }
   //elevator & shooter
   if(m_stick.getRawButton(3)){

    m_shooter.set(.7);
    Timer.delay(2);
    m_elevator.set(.4);
    

   }
   



  {
    if (m_stick.getRawButton(1) == true){
        m_intake.set(-.5);
    }
  else{
    m_intake.set(.0);
    }
  }
}

  // public void updateToggle()
  // {
  //     if(m_stick.getRawButton(1)){
  //         if(!togglePressed){
  //             toggleOn = !toggleOn;
  //             togglePressed = true;
  //         }
  //     }else{
  //         togglePressed = false;
  //     }

  //My attempt at arcade control




  
  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
