package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoTurnAtRadius;
import frc.robot.commands.CollectorPidPosition;
import frc.robot.commands.SetSweeperPower;

/**
 * These are the autonomous programs (commands) for our robot loaded with 3 balls to score those 3 from line positions
 * before moving on to a post-score autonomous program.
 */
public enum AutonomousScore {
  // This is score from aligned to the scoring portal
  SCORE("Score", "Score from the line, directly in front of the score portal", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_SCORE_DELAY),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new AutoDrive(72.0, Constants.AUTO_MOVE_SPEED) // approach bottom target
      ),
      new SetSweeperPower(Constants.AUTO_DISCHARGE_POWER), // set sweeper to dump power
      new WaitCommand(Constants.AUTO_DISCHARGE_TIME), // wait for the discharge time
      new SetSweeperPower(0.0) // set sweeper to stop - ready to do the next thing
  )),
  MID_FIELD("MidField", "Score from the line at mid field", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_SCORE_DELAY),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, 90.0, Constants.AUTO_TURN_SPEED, true, false), // 90 degrees clockwise at 30" radius
              new AutoDrive(7.75, Constants.AUTO_MOVE_SPEED, false, false), // 7.75" toward target
              new AutoTurnAtRadius(30.0, -90.0, Constants.AUTO_TURN_SPEED, false, false), // 90 counterclockwise  at 30" radius
              new AutoDrive(12.0, Constants.AUTO_MOVE_SPEED, false, true) // 12" toward target
          )
      ),
      new SetSweeperPower(Constants.AUTO_DISCHARGE_POWER), // set sweeper to dump
      new WaitCommand(Constants.AUTO_DISCHARGE_TIME), // wait for the discharge time
      new SetSweeperPower(0.0) // set sweeper to stop - ready to do the next thing
  )),
  HUMAN("Human", "Score from the line directly in front of the opposition human depot", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_SCORE_DELAY),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, 90.0, Constants.AUTO_TURN_SPEED, true, false), // 90 degrees clockwise at 30" radius
              new AutoDrive(69.5, Constants.AUTO_MOVE_SPEED, false, false), // 69.5" toward target
              new AutoTurnAtRadius(30.0, -90.0, Constants.AUTO_TURN_SPEED, false, false), // 90 counterclockwise  at 30" radius
              new AutoDrive(12.0, Constants.AUTO_MOVE_SPEED, false, true) // 12" toward target
          )
      ),
      new SetSweeperPower(Constants.AUTO_DISCHARGE_POWER), // set sweeper to dump
      new WaitCommand(Constants.AUTO_DISCHARGE_TIME), // wait for the discharge time
      new SetSweeperPower(0.0) // set sweeper to stop - ready to do the next thing
  )),
  TRENCH("Trench", "Score from the line directly in front of the trench", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_SCORE_DELAY),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION), // arm to dump position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, -90.0, Constants.AUTO_TURN_SPEED, true, false), // 90 counterclockwise  at 30" radius
              new AutoDrive(6.91, Constants.AUTO_MOVE_SPEED, false, false), // 6.91" toward target
              new AutoTurnAtRadius(30.0, 90.0, Constants.AUTO_TURN_SPEED, false, false), // 90 degrees clockwise at 30" radius
              new AutoDrive(12.0, Constants.AUTO_MOVE_SPEED, false, true) // 12" toward target
          )
      ),
      new SetSweeperPower(Constants.AUTO_DISCHARGE_POWER), // set sweeper to dump
      new WaitCommand(Constants.AUTO_DISCHARGE_TIME), // wait for the discharge time
      new SetSweeperPower(0.0) // set sweeper to stop - ready to do the next thing
  )),
  HIBERNATE("Hibernate", "From in front of the opposition trench - stay out of the way, prepare to play", new SequentialCommandGroup(
      new WaitCommand(Constants.AUTO_SCORE_DELAY),
      new ParallelCommandGroup(
          new CollectorPidPosition(Constants.ArmPosition.FLOOR_POSITION), // arm to dump position
          new SequentialCommandGroup( // the path and arm position are independent - so the entire path is here
              new AutoTurnAtRadius(30.0, -45.0, Constants.AUTO_TURN_SPEED, true, false), // 90 counterclockwise  at 30" radius
              new AutoDrive(12.0, Constants.AUTO_MOVE_SPEED, false, true) // 12" toward target
          )
      )
  )),
  NONE_FLOOR("(test)NoneFloor", "TEST ONLY - do nothing except put the collector on the floor",
      new CollectorPidPosition(Constants.ArmPosition.FLOOR_POSITION) // arm to dump position
  ),
  NONE_SCORE("(test)NoneScore", "TEST ONLY - do nothing except put the collector in score position",
      new CollectorPidPosition(Constants.ArmPosition.DELIVER_POSITION) // arm to dump position
  );


  public final String NAME;
  public final String DESCRIPTION;
  public final Command COMMAND;

  AutonomousScore(String name, String description, Command command) {
    NAME = name;
    DESCRIPTION = description;
    COMMAND = command;
  }

  static public String[] asStringArray() {
    AutonomousScore[] autos = AutonomousScore.values();
    String[] strings = new String[autos.length];
    for (int i = 0; i < autos.length; i++) {
      strings[i] = autos[i].NAME;
    }
    return strings;
  }

  static public String getDefaultName() {
    return SCORE.NAME;
  }

  static public AutonomousScore getDefault() {
    return SCORE;
  }
}
