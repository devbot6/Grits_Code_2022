// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;
import javax.lang.model.util.ElementScanner6;

import org.ejml.equation.Variable;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
// import java.io.PushbackInputStream;
// import javax.swing.ButtonGroup;
// import javax.swing.plaf.basic.BasicToggleButtonUI;
// import javax.xml.stream.events.StartElement;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
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
  private final PWMSparkMax m_arm = new PWMSparkMax(4);
  private final DoubleSolenoid p_redlights  = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 3, 2);
  private final DoubleSolenoid p_arms  = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  private final Compressor c_compressor = new Compressor(PneumaticsModuleType.CTREPCM);
  private final PWMSparkMax m_intake = new PWMSparkMax(5);
  private final PWMSparkMax m_climb = new PWMSparkMax(6);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive, m_rightDrive);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_Stick2 = new Joystick(1);
  private final Timer m_timer = new Timer();
  private final DigitalInput limitSwitchRight = new DigitalInput(5);
  private final DigitalInput limitSwitchLeft = new DigitalInput(4);
  boolean toggleOn = false;
  boolean togglePressed = false;
  boolean initShootClose = false;
  boolean initShootLow = false;
  boolean initShootHigh = false;
  double drivemotor = .7;
  //double shootermotor = ??
  double elemotor = .5;
  double intakeupmotor = -.85;
  double intakemotor = -.7;
  double climbmotorup = .6;
  double climbmotordown = -.6; 
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
    initShootClose = true;
    initShootLow = true;
    initShootHigh = true;
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
    shoot(initShootClose, 0, 0.6, -0.7);
  }
  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    m_timer.reset();
    m_timer.start();
     if(c_compressor.getPressureSwitchValue() == false){
       c_compressor.enableDigital();
     }
     else{
       c_compressor.disable();
     }
  }
   /** kForward value = red light (button ) kReverse value = blue light (button ) */
  /** This function is called periodically during teleoperated mode. */
  int currentTime;
  int startTimer;
  boolean testBool;
  boolean elevatorOff;
  @Override
  public void teleopPeriodic(){

  if(m_Stick2.getRawButton(1)){
    p_redlights.set(Value.kForward);
  }

  //lights
  if(m_Stick2.getRawButton(2)){
    p_redlights.set(Value.kReverse);
  }

  //arms up
  if(m_Stick2.getRawButton(7)){
    p_arms.set(Value.kForward);
  } 
  
  //arms down 
  if(m_Stick2.getRawButton(8)){
    p_arms.set(Value.kReverse);
  }

  //limit switches for intake
   if(limitSwitchRight.get() == false){
   }
   if(limitSwitchLeft.get() == false){
   }

   

   //elevator alone 
   if(m_stick.getRawButton(8) == true){
     m_elevator.set(elemotor);
   }
   else{
     m_elevator.set(0);
   }

//elevator
   if(m_stick.getRawButton(7) == true){
     m_elevator.set(elemotor);
   }
   else if(m_stick.getRawButton(8) == true){
    m_elevator.set(elemotor);
  }
    else{
     m_elevator.set(0);
   }


   //drive
      double xSpeed = m_Stick2.getRawAxis(1) * drivemotor;
      double ZRotation = m_Stick2.getRawAxis(2) * drivemotor;
      m_robotDrive.arcadeDrive(-xSpeed, ZRotation);

   //intake up
   if(m_stick.getRawButton(5) == true && limitSwitchRight.get()){
      m_arm.set(intakeupmotor); 
    }
    else{
      m_arm.set(0);
    }

    //intake down 
    if(m_stick.getRawButton(6) == true){
      m_arm.set(-intakeupmotor); 
    }
    else{
      m_arm.set(0);
    }

    

  //Intake
    if (m_stick.getRawButton(7) == true){
        m_intake.set(intakemotor);
    }
  else{
    m_intake.set(.0);
    }

 

  //Traversal up & down
  if(m_Stick2.getRawButton(2)){
    m_climb.set(climbmotorup);
  }
  else if(m_Stick2.getRawButton(4)){
    m_climb.set(climbmotordown);
  }
  else{
    m_climb.set(0);
  }

  // //elevator & shooter spin up High shot
  if(m_stick.getRawButton(4)){
    initShootHigh = true;
    m_timer.reset();
    m_timer.start();
  }
    shoot(initShootHigh, -0.4, 0.6, -0.7);

  //elevator & shooter spin up low shot
  if(m_stick.getRawButton(2)){
    initShootLow = true;
    m_timer.reset();
    m_timer.start();
  }
  shoot(initShootLow, -.4, .6, -.5);

  // //elevator & shooter spin up close(mid) shot
   if(m_stick.getRawButton(3)){
     initShootClose = true;
     m_timer.reset();
     m_timer.start();
   }
     shoot(initShootClose, -0.4, 0.6, -0.7);
}

public void shoot(boolean run, double downElevator, double upElevator, double shooterValue){
  if(run == true){
    testBool = true;
    elevatorOff = true;
  }
  if(m_timer.get()>0 && m_timer.get()<.5 && testBool == true){
    m_elevator.set(downElevator);
  }
  else if(m_timer.get()>1 && m_timer.get()<3 && testBool == true){
    m_shooter.set(shooterValue);
    if(m_timer.get()>2 && m_timer.get()<3 && testBool == true){
      m_elevator.set(upElevator);
  }
  }
  else if(elevatorOff == true){
    m_elevator.set(0);
    elevatorOff = false;
  }
  else{
    m_shooter.set(0);
    }
}
  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}
  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}