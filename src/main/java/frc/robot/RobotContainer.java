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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
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


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // The robot's subsystems - we get the instance here - even if they are not used - to assure they are created
    final DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
    final Port7Subsystem port7Subsystem = Port7Subsystem.getInstance();
    final Port8Subsystem port8Subsystem = Port8Subsystem.getInstance();
    final Port9Subsystem port9Subsystem = Port9Subsystem.getInstance();
    final Port10Subsystem port10Subsystem = Port10Subsystem.getInstance();

    // perform robot and driver initializations
    driveSubsystem.setRobot();
    // Set the default commands for subsystems
    driveSubsystem.setDefaultCommand(new DriveCommand(m_stick));
    //m_sweeperSubsystem.setDefaultCommand(m_runSweeper); // do this in teleop init instead
    port7Subsystem.setDefaultCommand(new Port7Command());
    port8Subsystem.setDefaultCommand(new Port8Command());
    port9Subsystem.setDefaultCommand(new Port9Command());
    port10Subsystem.setDefaultCommand(new Port10Command());
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
    m_xboxB.whenPressed(new SetNextDriver(this));

    m_button5.whenPressed(new BumpPort7Speed(0.1));
    m_button3.whenPressed(new BumpPort7Speed(-0.1));

    m_button6.whenPressed(new BumpPort8Speed(0.1));
    m_button4.whenPressed(new BumpPort8Speed(-0.1));

    m_button7.whenPressed(new BumpPort9Speed(0.1));
    m_button8.whenPressed(new BumpPort9Speed(-0.1));

    m_button9.whenPressed(new BumpPort10Speed(0.1));
    m_button10.whenPressed(new BumpPort10Speed(-0.1));

    m_thumb.whenPressed(new ToggleShift());

  }

  public void resetRobot() {
    DriveSubsystem.getInstance().setRobot();
  }

  public void resetDriver() {

  }

  public XboxController getXbox() {
    return m_xbox;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

}
