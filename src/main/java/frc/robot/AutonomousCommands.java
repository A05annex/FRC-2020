package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoTurn;
import frc.robot.commands.CollectorToPosition;
import frc.robot.commands.SetSweeperPower;


/**
 * The autonomous Command Programs, defined in the robot container
 */
public enum AutonomousCommands {
  POSITION1("Center", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(72, Constants.AUTO_MOVE_SPEED) // approach bottom target
      ),
      new SetSweeperPower(-1), // set sweeper to dump
      new WaitCommand(1), // wait one second
      new SetSweeperPower(0), // set sweeper to stop
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back up to clear target
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // 90 degrees clockwise
      new ParallelCommandGroup(
          new AutoDrive(-60, Constants.AUTO_MOVE_SPEED), // away from target and out of the way of other robot
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      )
  )),
  POSITION2("Right", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoTurn(-45, Constants.AUTO_TURN_SPEED) // turn 45 degrees counterclockwise
      ),
      new AutoDrive(93, Constants.AUTO_MOVE_SPEED), // approach bottom target
      new AutoTurn(45, Constants.AUTO_TURN_SPEED), // straighten on bottom target
      new AutoDrive(11, Constants.AUTO_MOVE_SPEED),  // drive into dumping range (test these inches)
      new SetSweeperPower(-1), // dump
      new WaitCommand(1), // dump
      new SetSweeperPower(0), // turn off collector
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back off of target
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // 90 degrees clockwise
      new ParallelCommandGroup(
          new AutoDrive(-60, Constants.AUTO_MOVE_SPEED), // away from target and out of the way of other robot
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      )
  )),
  POSITION3("Left", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(40, Constants.AUTO_MOVE_SPEED) // 40 in forward
      ),
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // turn 90 degrees clockwise
      new AutoDrive(120, Constants.AUTO_MOVE_SPEED), // 10 ft toward target
      new AutoTurn(-90, Constants.AUTO_TURN_SPEED), // turn counterclockwise 90 towards target
      new AutoDrive(32, Constants.AUTO_MOVE_SPEED),  // drive into dumping range
      new SetSweeperPower(-1), // dump
      new WaitCommand(1), // dump
      new SetSweeperPower(0), // turn off collector
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back off of target
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // 90 degrees clockwise
      new ParallelCommandGroup(
          new AutoDrive(-60, Constants.AUTO_MOVE_SPEED), // away from target and out of the way of other robot
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      )
  )),
  POSITION4("Trench", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.COLLECT_POSITION), // arm to collect position
          new AutoDrive(87, Constants.AUTO_MOVE_SPEED), // approach trench
          new SetSweeperPower(1) // set power for collection
      ),
      new AutoDrive(216, Constants.AUTO_MOVE_SPEED), // down trench and collect
      new SetSweeperPower(0), // stop collector
      new AutoDrive(-216, Constants.AUTO_MOVE_SPEED), // go back backwards
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
          new AutoTurn(160, Constants.AUTO_TURN_SPEED) // turn back towards target
      ),
      new AutoDrive(208, Constants.AUTO_MOVE_SPEED), // drive back to target
      new AutoTurn(20, Constants.AUTO_TURN_SPEED), // turn to target
      new AutoDrive(12, Constants.AUTO_MOVE_SPEED), // approach target
      new SetSweeperPower(-1), // dump
      new WaitCommand(1), // dump
      new SetSweeperPower(0), // turn off collector
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back off of target
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // 90 degrees clockwise
      new ParallelCommandGroup(
          new AutoDrive(-60, Constants.AUTO_MOVE_SPEED), // away from target and out of the way of other robot
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      )
  )),
  POSITION5("Full", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(72, Constants.AUTO_MOVE_SPEED) // approach bottom target
      ),
      new SetSweeperPower(-1), // set sweeper to dump
      new WaitCommand(1), // wait one second
      new SetSweeperPower(0), // set sweeper to stop
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back up to clear target
      new ParallelCommandGroup(
          new AutoTurn(160, Constants.AUTO_TURN_SPEED), // turn 160 degrees right
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION), // bucket down
          new SetSweeperPower(1) // set sweeper to collect
      ),
      new AutoDrive(208, Constants.AUTO_MOVE_SPEED), // drive to front of trench
      new AutoTurn(20, Constants.AUTO_TURN_SPEED), // turn to go down trench
      new AutoDrive(216, Constants.AUTO_MOVE_SPEED), // go down trench
      new SetSweeperPower(0), // stop sweeper
      new AutoDrive(-216, Constants.AUTO_MOVE_SPEED), // go back backwards
      new ParallelCommandGroup(
          new CollectorToPosition(Constants.ArmPosition.DELIVER_POSITION), // arm back to dump position
          new AutoTurn(160, Constants.AUTO_TURN_SPEED) // turn back towards target
      ),
      new AutoDrive(208, Constants.AUTO_MOVE_SPEED), // drive back to target
      new AutoTurn(20, Constants.AUTO_TURN_SPEED), // turn to target
      new AutoDrive(12, Constants.AUTO_MOVE_SPEED), // approach target
      new SetSweeperPower(-1), // dump
      new WaitCommand(1), // dump
      new SetSweeperPower(0), // turn off collector
      new AutoDrive(-12, Constants.AUTO_MOVE_SPEED), // back off of target
      new AutoTurn(90, Constants.AUTO_TURN_SPEED), // 90 degrees clockwise
      new ParallelCommandGroup(
          new AutoDrive(-60, Constants.AUTO_MOVE_SPEED), // away from target and out of the way of other robot
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      )
  )),
  POSITION6("Move", new SequentialCommandGroup(
      new WaitCommand(Constants.DELAY),
      new ParallelCommandGroup(
          new AutoDrive(12, Constants.AUTO_MOVE_SPEED), // drive off the line
          new CollectorToPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to collect position
      ))),
  POSITION7("10ft", new AutoDrive(120, Constants.AUTO_MOVE_SPEED));

  static public String[] asStringArray() {
    AutonomousCommands[] autos = AutonomousCommands.values();
    String[] strings = new String[autos.length];
    for (int i = 0; i < autos.length; i++) {
      strings[i] = autos[i].NAME;
    }
    return strings;
  }

  static public String getDefaultName() {
    return POSITION1.NAME;
  }

  static public AutonomousCommands getDefault() {
    return POSITION1;
  }
  public final String NAME;
  public Command COMMAND;

  AutonomousCommands(String name, Command command) {
    NAME = name;
    COMMAND = command;
  }
}
