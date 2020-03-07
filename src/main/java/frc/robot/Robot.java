/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SpinnerSubsystem;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
  private Limelight m_limelight;
  private SpinnerSubsystem m_wheel;
  private DriveSubsystem m_drive;
//  private SendableChooser<Constants.Robots> robotChooser = new SendableChooser<>();

  private void dashboardTelemetry(int port, String key, double var) {
    SmartDashboard.putString(String.format("DB/String %d", port), String.format("%s: %4.3f", key, var));
  }

  private void dashboardTelemetry(int port, String key, String var) {
    SmartDashboard.putString(String.format("DB/String %d", port), String.format("%s: %s", key, var));
  }

  private void dashboardTelemetry(int port, String key, boolean var) {
    SmartDashboard.putString(String.format("DB/String %d", port),
        String.format("%s: %s", key, var ? "on" : "off"));
  }

  private void smartTelemetry(String key, double var) {
    SmartDashboard.putNumber(key, var);
  }

  private void smartTelemetry(String key, String var) {
    SmartDashboard.putString(key, var);
  }

  private void smartTelemetry(String key, boolean var) {
    SmartDashboard.putBoolean(key, var);
  }

  // This is the color that the camera will see (90 degrees away from the actual color)
  private String getMessageToString() {
    String gameData = DriverStation.getInstance().getGameSpecificMessage();
    String output;
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'R' :
          output = "Blue";
          break;
        case 'G' :
          output = "Yellow";
          break;
        case 'B' :
          output = "Red";
          break;
        case 'Y' :
          output = "Green";
          break;
        default:
          output = "Corrupt";
          break;
      }
    } else {
      output = "None";
    }
    return output;
  }

  private void displayTelemetry() {

    //dashboardTelemetry(0, "robot", Constants.ROBOT.ROBOT_NAME);
    dashboardTelemetry(0, "delay", SmartDashboard.getNumber("DB/Slider 0", 0));
    dashboardTelemetry(5, "driver", Constants.DRIVER.DRIVER_NAME);
    dashboardTelemetry(1, "auto", m_robotContainer.getAutonomousCommand(SmartDashboard.getString("Auto Selector",
        AutonomousCommands.getDefaultName())).NAME);

    dashboardTelemetry(2, "drive gear", m_drive.getGear().toString());
    dashboardTelemetry(3, "arm enc", ArmSubsystem.getInstance().getPosition());
    dashboardTelemetry(4, "arm pwr", ArmSubsystem.getInstance().getPositionPower());
    
    /*
    dashboardTelemetry(7, "mode", m_limelight.getMode().toString());
    dashboardTelemetry(8, "stream", m_limelight.getStream().toString());
    */
    dashboardTelemetry(7, "left enc", m_robotContainer.getDrive().getLeftPosition());
    dashboardTelemetry(8, "right enc", m_robotContainer.getDrive().getRightPosition());

    dashboardTelemetry(9, "spinner enc", m_wheel.getEncoder());

    dashboardTelemetry(6, "color", getMessageToString());

  }

  private void displaySmartTelemetry() {
    smartTelemetry("delay", 0.0);
    smartTelemetry("driver", Constants.DRIVER.DRIVER_NAME);
    smartTelemetry("auto", m_robotContainer.getAutonomousCommand(SmartDashboard.getString("Auto Selector",
    AutonomousCommands.getDefaultName())).NAME);
    smartTelemetry("drive gear", m_drive.getGear().toString());
    smartTelemetry("arm enc", ArmSubsystem.getInstance().getPosition());
    smartTelemetry("arm pwr", ArmSubsystem.getInstance().getPositionPower());
    smartTelemetry("left enc", m_robotContainer.getDrive().getLeftPosition());
    smartTelemetry("right enc", m_robotContainer.getDrive().getRightPosition());
    smartTelemetry("color", getMessageToString());
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    // empty the telemetry display
    for (int i = 0; i < 10; i++) {
      SmartDashboard.putString(String.format("DB/String %d", i), " ");
    }
    SmartDashboard.putStringArray("Auto List", AutonomousCommands.asStringArray());


    NavX.getInstance().initializeHeadingAndNav();

    m_limelight = m_robotContainer.getLimelight();
    m_limelight.setMode(Limelight.MODE.DRIVE);

    m_wheel = m_robotContainer.getBigWheel();
    m_drive = m_robotContainer.getDrive();

//    robotChooser.setDefaultOption(Constants.Robots.COMPETITION_ROBOT.ROBOT_NAME, Constants.Robots.COMPETITION_ROBOT);
//    robotChooser.addOption(Constants.Robots.PRACTICE_ROBOT.ROBOT_NAME, Constants.Robots.PRACTICE_ROBOT);
//    SmartDashboard.putData("Robot Selection", robotChooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    displaySmartTelemetry(); // output telemetry
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
//    Constants.ROBOT = robotChooser.getSelected();
//    m_robotContainer.resetRobot();

    // reset pneumatics when disabling
    m_robotContainer.getLiftSubsystem().retractUpper();
    m_robotContainer.getLiftSubsystem().retractLower();
    m_robotContainer.getSpinnerLift().spinner_down();
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
//    Constants.ROBOT = robotChooser.getSelected();
//    m_robotContainer.resetRobot();
    Constants.DELAY = SmartDashboard.getNumber("delay", 0.0);

    NavX.getInstance().initializeHeadingAndNav();
    ArmSubsystem.getInstance().initializeArmEncoder();
    m_robotContainer.getDrive().setGear(Constants.DriveGears.FIRST);

    m_robotContainer.getLiftSubsystem().restoreLiftPressure();
    m_robotContainer.getLiftSubsystem().retractUpper();
    m_robotContainer.getLiftSubsystem().retractLower();
    m_robotContainer.getSpinnerLift().spinner_down();

    m_autonomousCommand = m_robotContainer.getAutonomousCommand(SmartDashboard.getString("Auto Selector",
        AutonomousCommands.getDefaultName())).COMMAND;

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
//    Constants.ROBOT = robotChooser.getSelected();
//    m_robotContainer.resetRobot();
    NavX.getInstance().initializeHeadingAndNav();

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    // set sweeper command at teleop init
    m_robotContainer.getSweeperSubsystem().setDefaultCommand(m_robotContainer.getSweeperCommand());

    // restore lift pressure
    m_robotContainer.getLiftSubsystem().restoreLiftPressure();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
//    Constants.ROBOT = robotChooser.getSelected();
//    m_robotContainer.resetRobot();

    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}