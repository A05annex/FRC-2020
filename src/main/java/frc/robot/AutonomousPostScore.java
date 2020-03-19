package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoTurnAtRadius;
import frc.robot.commands.CollectorPidPosition;
import frc.robot.commands.SetSweeperPower;

public enum AutonomousPostScore {

  SCORE_TO_TRENCH_HOLD("TrenchHold",
      "Go from the score position to the trench, pickup 5, hold before scoring", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_POST_SCORE_DELAY),
      new AutoDrive(-12.0, Constants.AUTO_MOVE_SPEED, true, false),               // 12" back away from score
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.FLOOR_POSITION), // arm to floor pickup position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, 90.0, -Constants.AUTO_TURN_SPEED, false, true), // 90 degrees clockwise at 30" radius
              new AutoDrive(57.41, Constants.AUTO_MOVE_SPEED, true, false) // 57.41" toward right wall
          )
      ),
      new ParallelCommandGroup(
          new SetSweeperPower(Constants.AUTO_PICKUP_POWER),
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, 90.0, Constants.AUTO_TURN_SPEED, false, false), // 90 degrees clockwise at 30" radius
              new AutoDrive(265.0, Constants.AUTO_MOVE_SPEED, false, true) // 265.0" through the trench to pickup balls
          )
      ),
      new ParallelCommandGroup(
          new SetSweeperPower(0.0),
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoDrive(-265.0, Constants.AUTO_MOVE_SPEED, true, false), // -265.0" through the trench toward delivery
              new AutoTurnAtRadius(30.0, -90.0, -Constants.AUTO_TURN_SPEED, false, false) // 90 degrees clockwise at 30" radius
          )
      ),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to floor pickup position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoDrive(-157.41, Constants.AUTO_MOVE_SPEED, false, true), // 57.41" away from right wall
              new AutoTurnAtRadius(30.0, 90.0, -Constants.AUTO_TURN_SPEED, true, true) // 90 degrees clockwise at 30" radius
           )
      )
  )),
  SCORE_TO_TRENCH_SCORE("TrenchScore", "Go from the score position to the trench, pickup 5, score", new SequentialCommandGroup(
      SCORE_TO_TRENCH_HOLD.COMMAND,
      new AutoDrive(12.0, Constants.AUTO_MOVE_SPEED, true, false),               // 12" in to score
      new SetSweeperPower(Constants.AUTO_DISCHARGE_POWER), // set sweeper to dump
      new WaitCommand(Constants.AUTO_DISCHARGE_TIME), // wait for the discharge time
      new SetSweeperPower(0.0) // set sweeper to stop - ready to do the next thing
  )),
  SCORE_TO_PICKUP("ToPickup", "Go from score position to the pickup position", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_POST_SCORE_DELAY)
      // TODO - finish this
  )),
  HIBERNATE_TO_PICKUP("HibToPickup", "Go from the hibernate initial move to the pickup position", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_POST_SCORE_DELAY),
      new AutoDrive(256.8, Constants.AUTO_MOVE_SPEED, true, false),               // 256.8" back toward pickup at 45 deg to field
      new AutoTurnAtRadius(30.0, 45.0, Constants.AUTO_TURN_SPEED, false, false),  // 45 degrees clockwise at 30" radius
      new AutoDrive(117.4, Constants.AUTO_MOVE_SPEED, false, true)                // 117.4" toward pickup
  )),
  DO_NOT_MOVE("DoNotMove", "Do not move after the initial score move", new SequentialCommandGroup(
      new WaitCommand(1.0)
  ));



  public final String NAME;
  public final String DESCRIPTION;
  public final Command COMMAND;

  AutonomousPostScore(String name, String description, Command command) {
    NAME = name;
    DESCRIPTION = description;
    COMMAND = command;
  }
}
