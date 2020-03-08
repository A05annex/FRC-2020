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
  private final JoystickButton m_topLL = new JoystickButton(this.m_stick, 3);
  private final JoystickButton m_topLR = new JoystickButton(this.m_stick, 4);
  private final JoystickButton m_topUL = new JoystickButton(this.m_stick, 5);
  private final JoystickButton m_topUR = new JoystickButton(this.m_stick, 6);
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
    final ArmSubsystem armSubsystem = ArmSubsystem.getInstance();
    final SpinnerSubsystem spinnerSubsystem = SpinnerSubsystem.getInstance();
    //noinspection ResultOfMethodCallIgnored
    SweeperSubsystem.getInstance();
    //noinspection ResultOfMethodCallIgnored
    LiftSubsystem.getInstance();
    //noinspection ResultOfMethodCallIgnored
    SpinnerLift.getInstance();

    // perform robot and driver initializations
    driveSubsystem.setRobot();
    // Set the default commands for subsystems
    driveSubsystem.setDefaultCommand(new DriveCommand(m_stick));
    //m_sweeperSubsystem.setDefaultCommand(m_runSweeper); // do this in teleop init instead
    armSubsystem.setDefaultCommand(new ManualCollector(m_xbox));
    spinnerSubsystem.setDefaultCommand(new RunSpinner(m_xbox));
    // Configure the button bindings
    configureButtonBindings();

    // autonomous speed variables, adjust as necessary

    /*
    // Auto Center: start lined up with goal as far forward as possible, dump and get out of the way.
    AutonomousCommands.POSITION1.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
                new AutoDrive(72, autoMoveSpeed) // approach bottom target
            ),
            new SetSweeperPower(-1), // set sweeper to dump
            new WaitCommand(1), // wait one second
            new SetSweeperPower(0), // set sweeper to stop
            new AutoDrive(-12, autoMoveSpeed), // back up to clear target
            new AutoTurn(90, autoTurnSpeed), // 90 degrees clockwise
            new ParallelCommandGroup(
                new AutoDrive(-60, autoMoveSpeed), // away from target and out of the way of other robot
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            )
        );

    // Auto Right: Start 20 inches away from the right wall, dump and get out of the way.
    AutonomousCommands.POSITION2.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
                new AutoTurn(-45, autoTurnSpeed) // turn 45 degrees counterclockwise
            ),
            new AutoDrive(93, autoMoveSpeed), // approach bottom target
            new AutoTurn(45, autoTurnSpeed), // straighten on bottom target
            new AutoDrive(11, autoMoveSpeed),  // drive into dumping range (test these inches)
            new SetSweeperPower(-1), // dump
            new WaitCommand(1), // dump
            new SetSweeperPower(0), // turn off collector
            new AutoDrive(-12, autoMoveSpeed), // back off of target
            new AutoTurn(90, autoTurnSpeed), // 90 degrees clockwise
            new ParallelCommandGroup(
                new AutoDrive(-60, autoMoveSpeed), // away from target and out of the way of other robot
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            )
        );

    // Auto Left: Start 10 ft to the left of target
    AutonomousCommands.POSITION3.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
                new AutoDrive(40, autoMoveSpeed) // 40 in forward
            ),
            new AutoTurn(90, autoTurnSpeed), // turn 90 degrees clockwise
            new AutoDrive(120, autoMoveSpeed), // 10 ft toward target
            new AutoTurn(-90, autoTurnSpeed), // turn counterclockwise 90 towards target
            new AutoDrive(32, autoMoveSpeed),  // drive into dumping range
            new SetSweeperPower(-1), // dump
            new WaitCommand(1), // dump
            new SetSweeperPower(0), // turn off collector
            new AutoDrive(-12, autoMoveSpeed), // back off of target
            new AutoTurn(90, autoTurnSpeed), // 90 degrees clockwise
            new ParallelCommandGroup(
                new AutoDrive(-60, autoMoveSpeed), // away from target and out of the way of other robot
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            )
        );

    // Auto Trench: Don't preload, start lined up with the trench with robot as far forward as possible.
    // Collect 5 balls from trench, return to target, dump and get out of the way.
    AutonomousCommands.POSITION4.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.COLLECT_POSITION), // arm to collect position
                new AutoDrive(87, autoMoveSpeed), // approach trench
                new SetSweeperPower(1) // set power for collection
            ),
            new AutoDrive(216, autoMoveSpeed), // down trench and collect
            new SetSweeperPower(0), // stop collector
            new AutoDrive(-216, autoMoveSpeed), // go back backwards
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
                new AutoTurn(160, autoTurnSpeed) // turn back towards target
            ),
            new AutoDrive(208, autoMoveSpeed), // drive back to target
            new AutoTurn(20, autoTurnSpeed), // turn to target
            new AutoDrive(12, autoMoveSpeed), // approach target
            new SetSweeperPower(-1), // dump
            new WaitCommand(1), // dump
            new SetSweeperPower(0), // turn off collector
            new AutoDrive(-12, autoMoveSpeed), // back off of target
            new AutoTurn(90, autoTurnSpeed), // 90 degrees clockwise
            new ParallelCommandGroup(
                new AutoDrive(-60, autoMoveSpeed), // away from target and out of the way of other robot
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            )
        );

    // Auto Full: Setup in front of goal as far forward as possible.
    // Dump, collect from the trench, and return to dump them.
    AutonomousCommands.POSITION5.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
                new AutoDrive(72, autoMoveSpeed) // approach bottom target
            ),
            new SetSweeperPower(-1), // set sweeper to dump
            new WaitCommand(1), // wait one second
            new SetSweeperPower(0), // set sweeper to stop
            new AutoDrive(-12, autoMoveSpeed), // back up to clear target
            new ParallelCommandGroup(
                new AutoTurn(160, autoTurnSpeed), // turn 160 degrees right
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION), // bucket down
                new SetSweeperPower(1) // set sweeper to collect
            ),
            new AutoDrive(208, autoMoveSpeed), // drive to front of trench
            new AutoTurn(20, autoTurnSpeed), // turn to go down trench
            new AutoDrive(216, autoMoveSpeed), // go down trench
            new SetSweeperPower(0), // stop sweeper
            new AutoDrive(-216, autoMoveSpeed), // go back backwards
            new ParallelCommandGroup(
                new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
                new AutoTurn(160, autoTurnSpeed) // turn back towards target
            ),
            new AutoDrive(208, autoMoveSpeed), // drive back to target
            new AutoTurn(20, autoTurnSpeed), // turn to target
            new AutoDrive(12, autoMoveSpeed), // approach target
            new SetSweeperPower(-1), // dump
            new WaitCommand(1), // dump
            new SetSweeperPower(0), // turn off collector
            new AutoDrive(-12, autoMoveSpeed), // back off of target
            new AutoTurn(90, autoTurnSpeed), // 90 degrees clockwise
            new ParallelCommandGroup(
                new AutoDrive(-60, autoMoveSpeed), // away from target and out of the way of other robot
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            )
        );

    // Auto Move: Go forward one foot to get off the starting line.
    AutonomousCommands.POSITION6.COMMAND =
        new SequentialCommandGroup(
            new WaitCommand(Constants.DELAY),
            new ParallelCommandGroup(
                new AutoDrive(12, autoMoveSpeed), // drive off the line
                new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
            ));

    // 10 feet forward
    AutonomousCommands.POSITION7.COMMAND = 
    new AutoDrive(m_driveSubsystem, 120, autoMoveSpeed);

    AutonomousCommands.TEST_TURN_AT_2_CLOCK.COMMAND =
        new AutoTurnAtRadius(24.0, 90.0, autoMoveSpeed);

    AutonomousCommands.TEST_TURN_AT_2_COUNTER.COMMAND = new AutoTurnAtRadius(24.0, -90.0, autoMoveSpeed);

    AutonomousCommands.TEST_TURN_AT_5_CLOCK.COMMAND = new AutoTurnAtRadius(60.0, 90.0, autoMoveSpeed);

    AutonomousCommands.TEST_TURN_AT_5_COUNTER.COMMAND = new AutoTurnAtRadius(60.0, -90.0, autoMoveSpeed);

    AutonomousCommands.TEST_S_TURN.COMMAND =
        new SequentialCommandGroup(
            new AutoTurnAtRadius(24.0, 90.0, 0.5, 0.2, 20.0, 0.5, 1.0), // turn 90 degrees clockwise
            new AutoDrive(72.0, autoMoveSpeed, 0.5, 5.0, 0.5, 10.0), // 10 ft toward target
            new AutoTurnAtRadius(24.0, -90.0, 0.5, 0.5, 1.0, 0.5, 30.0), // turn counterclockwise 90 towards target
            new AutoDrive(40.0, 0.5, 0.5,10.0, 0.15, 10.0) // 10 ft toward target
        );
     */
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

    /*
    m_topLL.whenPressed(new ToggleLimeLightStream(m_limelight));
    m_topUL.whenPressed(new ToggleLimeLightMode(m_limelight));
    */

    m_thumb.whenPressed(new ToggleShift());

    m_button12.whenPressed(new LiftCylinderControl(
        LiftCylinderControl.LOWER_CYLINDER, LiftCylinderControl.EXTENDED));
    m_button10.whenPressed(new LiftCylinderControl(
        LiftCylinderControl.UPPER_CYLINDER, LiftCylinderControl.EXTENDED));
    m_button9.whenPressed(new LiftCylinderControl(
        LiftCylinderControl.UPPER_CYLINDER, LiftCylinderControl.RETRACTED));
    m_button8.whenHeld(new RunWinch(1));
    m_button7.whenHeld(new RunWinch(-1));

    m_xboxLeftBumper.whenPressed(new SpinnerUpDown(SpinnerUpDown.Position.UP));
    m_xboxRightBumper.whenPressed(new SpinnerUpDown(SpinnerUpDown.Position.DOWN));

    m_xboxX.whenPressed(new SpinnerForCounts(1, -18000));

    m_xboxDpadUp.whenPressed(new CollectorPidPosition(Constants.ArmPosition.FLOOR_POSITION));
    m_xboxDpadDown.whenPressed(new CollectorPidPosition(Constants.ArmPosition.COLLECT_POSITION));
    m_xboxDpadLeft.whenPressed(new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION));
    m_xboxDpadRight.whenPressed(new CollectorPidPosition(Constants.ArmPosition.START_POSITION));

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
  public AutonomousCommands getAutonomousCommand(String autoCommandName) {
    AutonomousCommands[] autos = AutonomousCommands.values();
    for (AutonomousCommands auto : autos) {
      if (autoCommandName.equals(auto.NAME)) {
        return auto;
      }
    }
    return AutonomousCommands.getDefault();
  }

}
