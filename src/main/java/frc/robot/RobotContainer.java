/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.ColorTargets;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
  private final ArmSubsystem m_armSubsystem = ArmSubsystem.getInstance();
  private final SweeperSubsystem m_sweeperSubsystem = SweeperSubsystem.getInstance();
  private final Limelight m_limelight = new Limelight();
  private final LiftSubsystem m_liftSubsystem = new LiftSubsystem();
  private final SpinnerSubsystem m_spinnerSubsystem = new SpinnerSubsystem();
  private final SpinnerSolenoid m_spinnerSolenoid = new SpinnerSolenoid();

  // The driver station buttons
  // - the joystick and buttons
  private final Joystick m_stick = new Joystick(0);

  private final JoystickButton m_trigger = new JoystickButton(this.m_stick, 1);
  private final JoystickButton m_thumb = new JoystickButton(this.m_stick, 2);
  private final JoystickButton m_button3 = new JoystickButton(this.m_stick, 3);
  private final JoystickButton m_button4 = new JoystickButton(this.m_stick, 4);
  private final JoystickButton m_button5 = new JoystickButton(this.m_stick, 5);
  private final JoystickButton m_button6 = new JoystickButton(this.m_stick, 6);
  private final JoystickButton m_button7 = new JoystickButton(this.m_stick, 7);
  private final JoystickButton m_button8 = new JoystickButton(this.m_stick, 8);
  private final JoystickButton m_button9 = new JoystickButton(this.m_stick, 9);
  private final JoystickButton m_button10 = new JoystickButton(this.m_stick, 10);
  private final JoystickButton m_button11 = new JoystickButton(this.m_stick, 11);
  private final JoystickButton m_button12 = new JoystickButton(this.m_stick, 12);

  // - the xbox controller and buttons
  private final XboxController m_xbox = new XboxController(1);
  private final JoystickButton m_xboxA = new JoystickButton(m_xbox, 1);
  private final JoystickButton m_xboxB = new JoystickButton(m_xbox, 2);
  private final JoystickButton m_xboxX = new JoystickButton(m_xbox, 3);
  private final JoystickButton m_xboxY = new JoystickButton(m_xbox, 4);
  private final JoystickButton m_xboxLeftBumper = new JoystickButton(m_xbox, 5);
  private final JoystickButton m_xboxRightBumper = new JoystickButton(m_xbox, 6);

  // The robot's commands
  private final DriveCommand m_driveCommand = new DriveCommand(m_driveSubsystem, m_stick);
  private final RunSweeper m_runSweeper = new RunSweeper(m_sweeperSubsystem, m_xbox);
  private final ManualCollector m_manualCollector = new ManualCollector(m_armSubsystem, m_xbox);
  private final RunSpinner m_runSpinner = new RunSpinner(m_spinnerSubsystem, m_xbox);

  // Color sensor
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private final ColorMatch m_colorMatcher = new ColorMatch();


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // perform robot and driver initializations
    m_driveSubsystem.setRobot();
    // Set the default commands for subsystems
    m_driveSubsystem.setDefaultCommand(m_driveCommand);
    m_sweeperSubsystem.setDefaultCommand(m_runSweeper);
    m_armSubsystem.setDefaultCommand(m_manualCollector);
    m_spinnerSubsystem.setDefaultCommand(m_runSpinner);
    // Configure the button bindings
    configureButtonBindings();

    // color sensor stuff
    m_colorMatcher.addColorMatch(ColorTargets.BLUE_TARGET);
    m_colorMatcher.addColorMatch(ColorTargets.GREEN_TARGET);
    m_colorMatcher.addColorMatch(ColorTargets.RED_TARGET);
    m_colorMatcher.addColorMatch(ColorTargets.YELLOW_TARGET);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
//    m_trigger.whenHeld(new setBigWheelPower(m_wheel, 1));
//    m_thumb.whenPressed(new bigWheelToPosition(m_wheel, 1, -18000));
//    m_button3.whenPressed(new resetBigWheelEncoders(m_wheel));
//        xboxA.whenPressed(new SetNextRobot(this));
    m_xboxB.whenPressed(new SetNextDriver(this));

    m_button3.whenPressed(new SetLimelightMode(m_limelight, SetLimelightMode.DRIVER_MODE));
    m_button5.whenPressed(new SetLimelightMode(m_limelight, SetLimelightMode.VISION_MODE));

    m_button4.whenPressed(new SetCameraStream(m_limelight, SetCameraStream.LIMELIGHT_STREAM));
    m_button6.whenPressed(new SetCameraStream(m_limelight, SetCameraStream.SECONDARY_STREAM));
    m_thumb.whenPressed(new SetCameraStream(m_limelight, SetCameraStream.SIDE_BY_SIDE));

    m_button12.whenPressed(new ExtendLowerLift(m_liftSubsystem));
//    m_button11.whenPressed(new RetractLowerLift(m_liftSubsystem));
    m_button10.whenPressed(new ExtendUpperLift(m_liftSubsystem));
//    m_button9.whenPressed(new RetractUpperLift(m_liftSubsystem));
    m_button8.whenHeld(new RunWinch(m_liftSubsystem, 0.5));
    m_button7.whenHeld(new RunWinch(m_liftSubsystem, -0.5));

    m_xboxLeftBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.UP));
    m_xboxRightBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.DOWN));

    m_xboxX.whenPressed(new SpinnerForCounts(m_spinnerSubsystem, 1, -18000));

  }

  public void resetRobot() {
    m_driveSubsystem.setRobot();
  }

  public void resetDriver() {

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }

  public Limelight getLimelight() {
    return m_limelight;
  }

  public SpinnerSubsystem getBigWheel() {
    return m_spinnerSubsystem;
  }

  public ColorSensorV3 getColorSensor() {
    return m_colorSensor;
  }

  public String getColorAsString() {

    // color object with values
    Color detectedColor = m_colorSensor.getColor();

    String colorString;
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == ColorTargets.BLUE_TARGET) {
      colorString = "Blue";
    } else if (match.color == ColorTargets.RED_TARGET) {
      colorString = "Red";
    } else if (match.color == ColorTargets.GREEN_TARGET) {
      colorString = "Green";
    } else if (match.color == ColorTargets.YELLOW_TARGET) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

    return colorString;
  }
}
