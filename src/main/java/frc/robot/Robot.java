//nivens

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// cooper
package frc.robot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.cameraserver.*;
import edu.wpi.first.cscore.UsbCamera; 

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
  double drivemotor = .85;
  double elemotor = .5;
  double intakeupmotor = -.75;
  double intakemotor = -.7;
  double climbmotorup = .85;
  double climbmotordown = -.85; 
  UsbCamera cam1;
  CameraServer cam2;
  
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
    //initShootClose = false;//one ball auto
    initShootClose = true;//two ball auto
   // initShootLow = true;
   // initShootHigh = true;
   }
  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {//code that will run during the autonmous period
    //oneBallAuto();
    twoBallAuto();
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

     cam1 = cam2.startAutomaticCapture();
  }
   /** kForward value = red light (button ) kReverse value = blue light (button ) */
  /** This function is called periodically during teleoperated mode. */
  int currentTime;
  int startTimer;
  boolean testBool;
  boolean elevatorOff;
  boolean shooterOff;
  @Override
  public void teleopPeriodic(){

    
    //cam1.startAutomaticCapture();
  

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

   //turbo mode
   if(m_Stick2.getRawButton(6)){
     drivemotor = .95;
   }


   //drive
      double xSpeed = m_Stick2.getRawAxis(1) * drivemotor;
      double ZRotation = m_Stick2.getRawAxis(2) * drivemotor;
      m_robotDrive.arcadeDrive(-xSpeed, ZRotation);

   //intake up/down
   if(m_stick.getRawButton(5) == true && limitSwitchRight.get()){
      m_arm.set(intakeupmotor); 
    }
    else if(m_stick.getRawButton(6) == true){
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
  // if(m_stick.getRawButton(4)){
  //   initShootHigh = true;
  //   m_timer.reset();
  //   m_timer.start();
  // }
  if(m_stick.getRawButton(2)){
    initShootLow = true;
    initShootClose = false;
    m_timer.reset();
    m_timer.start();
  }
  else if(m_stick.getRawButton(3)){
    initShootClose = true;
    initShootLow = false;
    m_timer.reset();
    m_timer.start();
  }
    // shoot(initShootHigh, -0.16, 0.7, -0.9);
    shoot(initShootLow, -.16, .7, -.35);
    shoot(initShootClose, -0.16, 0.7, -0.81);
  
}

public void shoot(boolean run, double downElevator, double upElevator, double shooterValue){
  if(run == true){
    
   
    
  
  if(m_timer.get()>0 && m_timer.get()<.5){
    m_elevator.set(downElevator);
  }
  else if(m_timer.get()>1 && m_timer.get()<4){
    m_shooter.set(shooterValue);
    if(m_timer.get()>2 && m_timer.get()<4){
      m_elevator.set(upElevator);
  }
  }

   else if(m_timer.get() > 3){
     m_shooter.stopMotor();
   }
  }
}



//one ball in bot, pick up one behind, human player shoots
public void oneBallAuto(){
// Drive for 2 seconds
if (m_timer.get() < 2) {
  m_robotDrive.arcadeDrive(0.7, 0.0);
  m_intake.set(-.8);
}
else if(m_timer.get()>2 && m_timer.get()<5){
m_arm.set(.55);
m_intake.set(-.8);

}
else if(m_timer.get()>3 && m_timer.get()<4.6){
m_robotDrive.arcadeDrive(0, .7);
m_elevator.set(.7);
}
else if(m_timer.get()>7 && m_timer.get()<10
){
initShootClose = true;
}
  
else {
  m_elevator.set(0);
  m_intake.set(0);
  m_robotDrive.stopMotor();
   // stop robot

}
shoot(initShootClose, 0, 0.7, -0.85);
}


//2 balls in robot to start 
public void twoBallAuto(){

  if (m_timer.get() < 1.75) {
    m_robotDrive.arcadeDrive(-0.65, 0.0);
  }  
  else if(m_timer.get()>1.75 && m_timer.get()<2){
    m_robotDrive.arcadeDrive(.65, 0);
  }
  else if(m_timer.get()>4 && m_timer.get()<5.25){
    m_robotDrive.arcadeDrive(-0.65, 0);
  }
  else{
    m_robotDrive.stopMotor();
    m_elevator.stopMotor();
  }
  shoot(initShootClose, 0, 0.7, -0.85);

}
  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}
  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}