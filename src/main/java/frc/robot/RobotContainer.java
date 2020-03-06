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
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

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

  // Sensor initialization.
  private final NavX m_navx = NavX.getInstance();

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
    //m_sweeperSubsystem.setDefaultCommand(m_runSweeper); // do this in teleop init instead
    m_armSubsystem.setDefaultCommand(m_manualCollector);
    m_spinnerSubsystem.setDefaultCommand(m_runSpinner);
    // Configure the button bindings
    configureButtonBindings();

    // autonomous speed variables, adjust as neccessary
    double autoMoveSpeed = 0.75;
    double autoTurnSpeed = 0.4;

    // Auto Center: start lined up with goal as far forward as possible, dump and get out of the way.
    AutonomousCommands.POSITION1.COMMAND = 
      new SequentialCommandGroup(
        new WaitCommand(Constants.DELAY),
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(m_driveSubsystem, 72, autoMoveSpeed) // approach bottom target
        ),
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // set sweeper to dump
        new WaitCommand(1), // wait one second
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // set sweeper to stop
        new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back up to clear target
        new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // 90 degrees clockwise
        new ParallelCommandGroup(
          new AutoDrive(m_driveSubsystem, -60, autoMoveSpeed), // away from target and out of the way of other robot
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
        )
      );

    // Auto Right: Start 20 inches away from the right wall, dump and get out of the way.
    AutonomousCommands.POSITION2.COMMAND = 
      new SequentialCommandGroup(
          new WaitCommand(Constants.DELAY),
          new ParallelCommandGroup(
            new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm to dump position 
            new AutoTurn(m_driveSubsystem, -45, autoTurnSpeed) // turn 45 degrees counterclockwise
          ),      
          new AutoDrive(m_driveSubsystem, 93, autoMoveSpeed), // approach bottom target
          new AutoTurn(m_driveSubsystem, 45, autoTurnSpeed), // straighten on bottom target
          new AutoDrive(m_driveSubsystem, 11, autoMoveSpeed),  // drive into dumping range (test these inches)
          new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // dump
          new WaitCommand(1), // dump
          new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // turn off collector
          new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back off of target
          new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // 90 degrees clockwise
        new ParallelCommandGroup(
          new AutoDrive(m_driveSubsystem, -60, autoMoveSpeed), // away from target and out of the way of other robot
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
        )
      );
    
    // Auto Left: Start 10 ft to the left of target
    AutonomousCommands.POSITION3.COMMAND = 
      new SequentialCommandGroup(
        new WaitCommand(Constants.DELAY),
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm to dump position 
          new AutoDrive(m_driveSubsystem, 40, autoMoveSpeed) // 40 in forward
        ),
        new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // turn 90 degrees clockwise
        new AutoDrive(m_driveSubsystem, 120, autoMoveSpeed), // 10 ft toward target
        new AutoTurn(m_driveSubsystem, -90, autoTurnSpeed), // turn counterclockwise 90 towards target
        new AutoDrive(m_driveSubsystem, 32, autoMoveSpeed),  // drive into dumping range
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // dump
        new WaitCommand(1), // dump
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // turn off collector
        new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back off of target
        new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // 90 degrees clockwise
        new ParallelCommandGroup(
          new AutoDrive(m_driveSubsystem, -60, autoMoveSpeed), // away from target and out of the way of other robot
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
        )
      );

    // Auto Trench: Don't preload, start lined up with the trench with robot as far forward as possible.
    // Collect 5 balls from trench, return to target, dump and get out of the way.
    AutonomousCommands.POSITION4.COMMAND =
      new SequentialCommandGroup(
        new WaitCommand(Constants.DELAY),
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.COLLECT_POSITION), // arm to collect position
          new AutoDrive(m_driveSubsystem, 87, autoMoveSpeed), // approach trench
          new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 1) // set power for collection
        ),
        new AutoDrive(m_driveSubsystem, 216, autoMoveSpeed), // down trench and collect
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // stop collector
        new AutoDrive(m_driveSubsystem, -216, autoMoveSpeed), // go back backwards
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
          new AutoTurn(m_driveSubsystem, 160, autoTurnSpeed) // turn back towards target
        ),
        new AutoDrive(m_driveSubsystem, 208, autoMoveSpeed), // drive back to target
        new AutoTurn(m_driveSubsystem, 20, autoTurnSpeed), // turn to target
        new AutoDrive(m_driveSubsystem, 12, autoMoveSpeed), // approach target
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // dump
        new WaitCommand(1), // dump
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // turn off collector
        new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back off of target
        new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // 90 degrees clockwise
        new ParallelCommandGroup(
          new AutoDrive(m_driveSubsystem, -60, autoMoveSpeed), // away from target and out of the way of other robot
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
        )
      );
    
    // Auto Full: Setup in front of goal as far forward as possible.
    // Dump, collect from the trench, and return to dump them.
    AutonomousCommands.POSITION5.COMMAND = 
      new SequentialCommandGroup(
        new WaitCommand(Constants.DELAY),
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(m_driveSubsystem, 72, autoMoveSpeed) // approach bottom target
        ),
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // set sweeper to dump
        new WaitCommand(1), // wait one second
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // set sweeper to stop
        new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back up to clear target
        new ParallelCommandGroup(
          new AutoTurn(m_driveSubsystem, 160, autoTurnSpeed), // turn 160 degrees right
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION), // bucket down
          new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 1) // set sweeper to collect
        ),
        new AutoDrive(m_driveSubsystem, 208, autoMoveSpeed), // drive to front of trench
        new AutoTurn(m_driveSubsystem, 20, autoTurnSpeed), // turn to go down trench
        new AutoDrive(m_driveSubsystem, 216, autoMoveSpeed), // go down trench
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // stop sweeper
        new AutoDrive(m_driveSubsystem, -216, autoMoveSpeed), // go back backwards
        new ParallelCommandGroup(
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
          new AutoTurn(m_driveSubsystem, 160, autoTurnSpeed) // turn back towards target
        ),
        new AutoDrive(m_driveSubsystem, 208, autoMoveSpeed), // drive back to target
        new AutoTurn(m_driveSubsystem, 20, autoTurnSpeed), // turn to target
        new AutoDrive(m_driveSubsystem, 12, autoMoveSpeed), // approach target
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, -1), // dump
        new WaitCommand(1), // dump
        new SetSweeperPower(m_sweeperSubsystem, m_spinnerSubsystem, 0), // turn off collector
        new AutoDrive(m_driveSubsystem, -12, autoMoveSpeed), // back off of target
        new AutoTurn(m_driveSubsystem, 90, autoTurnSpeed), // 90 degrees clockwise
        new ParallelCommandGroup(
          new AutoDrive(m_driveSubsystem, -60, autoMoveSpeed), // away from target and out of the way of other robot
          new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
        )
      );

    // Auto Move: Go forward one foot to get off the starting line.
    AutonomousCommands.POSITION6.COMMAND = 
    new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
        new AutoDrive(m_driveSubsystem, 12, autoMoveSpeed), // drive off the line
        new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
    ));

    // 10 feet forward
    AutonomousCommands.POSITION7.COMMAND = 
    new AutoDrive(m_driveSubsystem, 120, autoMoveSpeed);
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

    m_thumb.whenPressed(new ToggleShift(m_driveSubsystem));

    m_button12.whenPressed(new BumpArmKp(0.01));
    m_button11.whenPressed(new BumpArmKp(-0.01));
    m_button10.whenPressed(new BumpArmKi(0.001));
    m_button9.whenPressed(new BumpArmKi(-0.001));
    m_button8.whenPressed(new BumpArmKd(0.001));
    m_button7.whenPressed(new BumpArmKd(-0.001));

    /*
    m_button12.whenPressed(new LiftCylinderControl(m_liftSubsystem, m_spinnerSolenoid,
        LiftCylinderControl.LOWER_CYLINDER, LiftCylinderControl.EXTENDED));
    m_button10.whenPressed(new LiftCylinderControl(m_liftSubsystem, m_spinnerSolenoid,
        LiftCylinderControl.UPPER_CYLINDER, LiftCylinderControl.EXTENDED));
    m_button9.whenPressed(new LiftCylinderControl(m_liftSubsystem, m_spinnerSolenoid,
        LiftCylinderControl.UPPER_CYLINDER, LiftCylinderControl.RETRACTED));
    m_button8.whenHeld(new RunWinch(m_liftSubsystem, 1));
    m_button7.whenHeld(new RunWinch(m_liftSubsystem, -1));
    */

    m_xboxLeftBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.UP));
    m_xboxRightBumper.whenPressed(new SpinnerUpDown(m_spinnerSolenoid, SpinnerUpDown.Position.DOWN));

    m_xboxX.whenPressed(new SpinnerForCounts(m_spinnerSubsystem, 1, -18000));

    m_xboxDpadUp.whenPressed(new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.FLOOR_POSITION));
    m_xboxDpadDown.whenPressed(new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.COLLECT_POSITION));
    m_xboxDpadLeft.whenPressed(new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.DELIVER_POSITION));
    m_xboxDpadRight.whenPressed(new CollectorToPosition(m_armSubsystem, Constants.ArmPosition.START_POSITION));

  }

  public void resetRobot() {
    m_driveSubsystem.setRobot();
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
    AutonomousCommands autos[] = AutonomousCommands.values();
    for (int i = 0; i < autos.length; i++) {
      if (autoCommandName.equals(autos[i].NAME)) {
        return autos[i];
      }
    }
    return AutonomousCommands.getDefault();
  }

  public Limelight getLimelight() {
    return m_limelight;
  }

  public SpinnerSubsystem getBigWheel() {
    return m_spinnerSubsystem;
  }

  public DriveSubsystem getDrive() {
    return m_driveSubsystem;
  }

  public SweeperSubsystem getSweeperSubsystem() {
    return m_sweeperSubsystem;
  }
  
  public RunSweeper getSweeperCommand() {
    return m_runSweeper;
  }

  public LiftSubsystem getLiftSubsystem() {
    return m_liftSubsystem;
  }

  public SpinnerSolenoid getSpinnerLift() {
    return m_spinnerSolenoid;
  }

  public  ArmSubsystem getArm() {
    return m_armSubsystem;
  }
}
