/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
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
  private final POVButton m_xboxDpadUp = new POVButton(m_xbox, 0);
  private final POVButton m_xboxDpadLeft = new POVButton(m_xbox, 270);
  private final POVButton m_xboxDpadDown = new POVButton(m_xbox, 180);
  private final POVButton m_xboxDpadRight = new POVButton(m_xbox, 90);

  // The robot's commands
  private final DriveCommand m_driveCommand = new DriveCommand(m_driveSubsystem, m_stick);
  private final RunSweeper m_runSweeper = new RunSweeper(m_sweeperSubsystem, m_xbox);
  private final ManualCollector m_manualCollector = new ManualCollector(m_armSubsystem, m_xbox);
  private final RunSpinner m_runSpinner = new RunSpinner(m_spinnerSubsystem, m_xbox);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // perform robot and driver initializations
    m_driveSubsystem.setRobot();
    // Set the default commands for subsystems
    m_driveSubsystem.setDefaultCommand(m_driveCommand);
    m_sweeperSubsystem.setDefaultCommand(m_runSweeper);
    //m_armSubsystem.setDefaultCommand(m_manualCollector);
    m_spinnerSubsystem.setDefaultCommand(m_runSpinner);
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
//        m_xboxA.whenPressed(new SetNextRobot(this));
    /*
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
    m_button8.whenHeld(new RunWinch(m_liftSubsystem, 1));
    m_button7.whenHeld(new RunWinch(m_liftSubsystem, -1));

    m_xboxLeftBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.UP));
    m_xboxRightBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.DOWN));

    m_xboxX.whenPressed(new SpinnerForCounts(m_spinnerSubsystem, 1, -18000));

    m_xboxDpadUp.whenPressed(new CollectorToPosition(m_armSubsystem, 500));
    m_xboxDpadDown.whenPressed(new CollectorToPosition(m_armSubsystem, 43000));
    m_xboxDpadLeft.whenPressed(new CollectorToPosition(m_armSubsystem, 32000));
    */

    m_trigger.whenPressed(m_driveCommand); // emergency stop auto commands

    // full auto 1
    double auto1Speed = 0.5;
    m_button11.whenPressed(
      new SequentialCommandGroup(
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, 32000), // arm to dump position
          new AutoDrive(m_driveSubsystem, 72, auto1Speed) // approach bottom target
        ),
        new SetSweeperPower(m_sweeperSubsystem, -1), // set sweeper to dump
        new WaitCommand(1), // wait one second
        new AutoDrive(m_driveSubsystem, -12, auto1Speed), // back up to clear target
        new ParallelCommandGroup(
          new AutoTurn(m_driveSubsystem, 160, auto1Speed), // turn 160 degrees right
          new CollectorToPosition(m_armSubsystem, 500), // bucket down
          new SetSweeperPower(m_sweeperSubsystem, 1) // set sweeper to collect
        ),
        new AutoDrive(m_driveSubsystem, 208, auto1Speed), // drive to front of trench
        new AutoTurn(m_driveSubsystem, 20, auto1Speed), // turn to go down trench
        new AutoDrive(m_driveSubsystem, 216, auto1Speed), // go down trench
        new AutoDrive(m_driveSubsystem, -216, auto1Speed), // go back backwards
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, 32000), // arm back to dump position
          new AutoTurn(m_driveSubsystem, 160, auto1Speed) // turn back towards target
        ),
        new AutoDrive(m_driveSubsystem, 208, auto1Speed), // drive back to target
        new AutoTurn(m_driveSubsystem, 20, auto1Speed), // turn to target
        new AutoDrive(m_driveSubsystem, 12, auto1Speed), // approach target
        new SetSweeperPower(m_sweeperSubsystem, -1) // set sweeper to dump
    ));

    m_button7.whenPressed(new CollectorToPosition(m_armSubsystem, 32000));
    m_button8.whenPressed(new AutoDrive(m_driveSubsystem, 72, 0.5));
    m_button9.whenPressed(new AutoTurn(m_driveSubsystem, 160, 0.5));
    m_button10.whenPressed(new SetSweeperPower(m_sweeperSubsystem, -1));
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

  public DriveSubsystem getDriveSubsystem() {
    return m_driveSubsystem;
  }

}
