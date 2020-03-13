/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.RunSweeper;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;
  private DriveSubsystem m_driveSubsystem;
  private LiftSubsystem m_liftSubsystem;
  private SpinnerLift m_spinnerLift;
  private SweeperSubsystem m_sweeperSubsystem;
  private NavX m_navx;
  private Limelight m_limelight;
  private SpinnerSubsystem m_spinner;
  private Command m_autonomousCommand;

  private double m_lastHeading = -1.0;
  private double m_lastRawYaw = -1.0;
  private double m_lastYawDrift = -1.0;

  private double m_lastAutonomousDelay = -2.0;
  private double m_lastArmPosition = -2.0;
  private double m_lastArmPower = -2.0;
  private double m_lastDriveLeftEnc = -2.0;
  private double m_lastDriveRightEnc = -2.0;
  private double m_lastSpinnerEnc = -2.0;
  private String m_lastDriver = "";
  private String m_lastAuto = "";
  private String m_lastGear = "";
  private String m_lastColor = "";
//  private SendableChooser<Constants.Robots> robotChooser = new SendableChooser<>();

  /**
   * Update telemetry feedback for a real number value. If the value has not changed, no update is sent
   *
   * @param port      (int) The port 0 - 9 to write to.
   * @param key       (String) The key for the telemetry.
   * @param var       (double) The number to be reported.
   * @param lastValue (double) The last value reported.
   * @return (double) Returns {@code var}
   */
  @SuppressWarnings("unused")
  private double dashboardTelemetry(int port, String key, double var, double lastValue) {
    if (var != lastValue) {
      SmartDashboard.putString(String.format("DB/String %d", port), String.format("%s: %4.4f", key, var));
    }
    return var;
  }

  /**
   * Update telemetry feedback for an integer value. If the value has not changed, no update is sent
   *
   * @param port      (int) The port 0 - 9 to write to.
   * @param key       (String) The key for the telemetry.
   * @param var       (int) The integer to be reported.
   * @param lastValue (int) The last value reported.
   * @return (int) Returns {@code var}
   */
  @SuppressWarnings("unused")
  private int dashboardTelemetry(int port, String key, int var, int lastValue) {
    if (var != lastValue) {
      SmartDashboard.putString(String.format("DB/String %d", port), String.format("%s: %d", key, var));
    }
    return var;
  }

  /**
   * Update telemetry feedback for a string value. If the value has not changed, no update is sent
   *
   * @param port      (int) The port 0 - 9 to write to.
   * @param key       (String) The key for the telemetry.
   * @param var       (String) The string to be reported.
   * @param lastValue (String) The last value reported.
   * @return (String) Returns {@code var}
   */
  @SuppressWarnings("unused")
  private String dashboardTelemetry(int port, String key, String var, String lastValue) {
    if (!var.equals(lastValue)) {
      SmartDashboard.putString(String.format("DB/String %d", port), String.format("%s: %s", key, var));
    }
    return var;
  }

  /**
   * Update telemetry feedback for a boolean value. If the value has not changed, no update is sent
   *
   * @param port      (int) The port 0 - 9 to write to.
   * @param key       (String) The key for the telemetry.
   * @param var       (boolean) The boolean to be reported.
   * @param lastValue (boolean) The last value reported.
   * @return (boolean) Returns {@code var}
   */
  @SuppressWarnings("unused")
  private boolean dashboardTelemetry(int port, String key, boolean var, boolean lastValue) {
    if (var != lastValue) {
      SmartDashboard.putString(String.format("DB/String %d", port),
          String.format("%s: %s", key, var ? "on" : "off"));
    }
    return var;
  }

  // This is the color that the camera will see (90 degrees away from the actual color)
  private String getMessageToString() {
    String gameData = DriverStation.getInstance().getGameSpecificMessage();
    String output;
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'R':
          output = "Blue";
          break;
        case 'G':
          output = "Yellow";
          break;
        case 'B':
          output = "Red";
          break;
        case 'Y':
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
    m_lastAutonomousDelay = dashboardTelemetry(0, "delay", SmartDashboard.getNumber("DB/Slider 0", 0), m_lastAutonomousDelay);
    m_lastDriver = dashboardTelemetry(5, "driver", Constants.DRIVER.DRIVER_NAME, m_lastDriver);
    m_lastAuto = dashboardTelemetry(1, "auto",
        SmartDashboard.getString("Auto Selector", AutonomousCommands.getDefaultName()), m_lastAuto);

//    m_lastGear = dashboardTelemetry(2, "drive gear", m_driveSubsystem.getGear().toString(), m_lastGear);
//    m_lastArmPosition = dashboardTelemetry(3, "arm enc", ArmSubsystem.getInstance().getPosition(), m_lastArmPosition);
//    m_lastArmPower = dashboardTelemetry(4, "expected", ArmSubsystem.getInstance().getPositionPower(), m_lastArmPower);

    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    NavX.NavInfo navInfo = m_navx.getNavInfo();
    m_lastHeading = dashboardTelemetry(2, "heading", headingInfo.heading, m_lastHeading);
    m_lastArmPosition = dashboardTelemetry(3, "yaw", navInfo.rawYaw, m_lastRawYaw);
    m_lastYawDrift = dashboardTelemetry(4, "yaw drift", m_navx.getYawDrift(), m_lastYawDrift);
    
    /*
    dashboardTelemetry(7, "mode", m_limelight.getMode().toString());
    dashboardTelemetry(8, "stream", m_limelight.getStream().toString());
    */
    m_lastDriveLeftEnc = dashboardTelemetry(7, "left enc", m_driveSubsystem.getLeftPosition(), m_lastDriveLeftEnc);
    m_lastDriveRightEnc = dashboardTelemetry(8, "right enc", m_driveSubsystem.getRightPosition(), m_lastDriveRightEnc);

    m_lastSpinnerEnc = dashboardTelemetry(9, "heading", m_navx.getHeadingInfo().heading, m_lastSpinnerEnc);

    m_lastColor = dashboardTelemetry(6, "color", getMessageToString(), m_lastColor);

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
    // Get these here so you know the RobotContainer has been created and all of the subsystems instantiated by that.
    m_driveSubsystem = DriveSubsystem.getInstance();
    m_liftSubsystem = LiftSubsystem.getInstance();
    m_spinnerLift = SpinnerLift.getInstance();
    m_sweeperSubsystem = SweeperSubsystem.getInstance();
    m_navx = NavX.getInstance();
    m_limelight = Limelight.getInstance();
    m_spinner = SpinnerSubsystem.getInstance();

    // empty the telemetry display
    for (int i = 0; i < 10; i++) {
      SmartDashboard.putString(String.format("DB/String %d", i), " ");
    }
    SmartDashboard.putStringArray("Auto List", AutonomousCommands.asStringArray());


    m_navx.initializeHeadingAndNav();

    m_limelight.setMode(Limelight.MODE.DRIVE);

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
    displayTelemetry(); // output telemetry
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
//    Constants.ROBOT = robotChooser.getSelected();
//    m_robotContainer.resetRobot();

    // reset pneumatics when disabling
    m_liftSubsystem.dumpLiftPressure();
    m_spinnerLift.spinner_down();
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
    Constants.DELAY = SmartDashboard.getNumber("DB/Slider 0", 0.0);

    m_navx.initializeHeadingAndNav();
    ArmSubsystem.getInstance().initializeArmEncoder();
    m_driveSubsystem.setGear(Constants.DriveGears.FIRST);

    // Make sure everything that should be is at initial state
    m_liftSubsystem.retractUpper();
    m_liftSubsystem.retractLower();
    m_liftSubsystem.restoreLiftPressure();
    m_spinnerLift.spinner_down();

    m_autonomousCommand = m_robotContainer.getAutonomousCommand(SmartDashboard.getString("Auto Selector",
        AutonomousCommands.getDefaultName())).COMMAND;

    // schedule the autonomous command
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
    m_navx.initializeHeadingAndNav(); //TODO: comment this out so field relative drive works

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    // set sweeper command at teleop init
    m_sweeperSubsystem.setDefaultCommand(new RunSweeper(m_robotContainer.getXbox()));

    // Make sure everything that should be is at initial state
    m_liftSubsystem.retractUpper();
    m_liftSubsystem.retractLower();
    m_liftSubsystem.restoreLiftPressure();
    m_spinnerLift.spinner_down();
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