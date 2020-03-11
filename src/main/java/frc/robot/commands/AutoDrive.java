/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RampInOut;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This is a command to drive straight using the IMU (HavX) to keep the robot heading in the expected heading.
 */
public class AutoDrive extends CommandBase {

  private final DriveSubsystem m_driveSubsystem = DriveSubsystem.getInstance();
  private final double m_distance;
  private final double m_speed;
  private double m_minAtStart = Constants.AUTO_MOVE_MIN_ACCEL;
  private double m_accelerationDistance = Constants.AUTO_MOVE_ACCEL_DIST;
  private double m_minAtEnd = Constants.AUTO_MOVE_MIN_DECEL;
  private double m_decelerationDistance = Constants.AUTO_MOVE_DECEL_DIST;
  private double m_startEncoder;
  private RampInOut m_ramp;
  private double m_directionMult;

  /**
   * Drives straight forward/backward, from a stopped position to a stopped position, a specified distance in inches, at the
   * default autonomous move speed.
   *
   * @param distanceInInches (double) Distance to travel in inches, positive is forward, negative is backwards.
   */
  public AutoDrive(double distanceInInches) {
    this(distanceInInches, Constants.AUTO_MOVE_SPEED);
  }
  /**
   * Drives straight forward/backward for a specified distance in inches, at a specified maximum speed.
   *
   * @param distanceInInches (double) Distance to travel in inches, positive is forward, negative is backwards.
   * @param speed            (double) Maximum speed from 0 to 1. Do not make this negative!
   */
  public AutoDrive(double distanceInInches, double speed) {
    m_distance = distanceInInches * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TICS_PER_INCH;
    m_speed = Math.abs(speed);
    // reverse speed if distance is negative
    m_directionMult = (distanceInInches < 0.0) ? -1.0 : 1.0;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  /**
   * Drives straight forward/backward for a specified distance in inches, at a specified maximum speed, using the specified
   * acceleration and deceleration parameters to control the speed ramp.
   *
   * @param distanceInInches (double) Distance to travel in inches, positive is forward, negative is backwards.
   * @param speed            (double) Maximum speed from 0 to 1. Do not make this negative!
   * @param minAtStart
   * @param accelerationDistance
   * @param minAtEnd
   * @param decelerationDistance
   */
  public AutoDrive(double distanceInInches, double speed, double minAtStart,
                   double accelerationDistance, double minAtEnd, double decelerationDistance) {
    this(distanceInInches, speed);
    m_minAtStart = minAtStart;
    m_accelerationDistance = accelerationDistance;
    m_minAtEnd = minAtEnd;
    m_decelerationDistance = decelerationDistance;
  }

  /**
   * Drives straight forward/backward for a specified distance in inches, at a specified maximum speed, using the default
   * accelerations and decelerations depending on whether the move starts/stops at rest; or, is the continuation of a
   * previous move.
   *
   * @param distanceInInches (double) Distance to travel in inches, positive is forward, negative is backwards.
   * @param fromStop (boolean) {@code true} if this move is starting from a stopped position, {@code false} if this is
   *                 the continuation of another move or turn at radius.
   * @param toStop (boolean) {@code true} if this move is ending in a stopped position, {@code false} if there is
   *               another move or turn at radius following this.
   */
  public AutoDrive(double distanceInInches, boolean fromStop, boolean toStop) {
    this(distanceInInches, Constants.AUTO_MOVE_SPEED, fromStop, toStop);
  }

  /**
   * Drives straight forward/backward for a specified distance in inches, at a specified maximum speed, using the default
   * accelerations and decelerations depending on whether the move starts/stops at rest, or, is the continuation of a
   * previous move.
   *
   * @param distanceInInches (double) Distance to travel in inches, positive is forward, negative is backwards.
   * @param fromStop (boolean) {@code true} if this move is starting from a stopped position, {@code false} if this is
   *                 the continuation of another move or turn at radius.
   * @param toStop (boolean) {@code true} if this move is ending in a stopped position, {@code false} if there is
   *               another move or turn at radius following this.
   */
  public AutoDrive(double distanceInInches, double speed, boolean fromStop, boolean toStop) {
    this(distanceInInches, speed);
    m_minAtStart = fromStop ? Constants.AUTO_MOVE_MIN_ACCEL : Constants.AUTO_INTO_NEXT_MIN_ACCEL;
    m_accelerationDistance = fromStop ? Constants.AUTO_MOVE_ACCEL_DIST : Constants.AUTO_INTO_NEXT_ACCEL_DIST;
    m_minAtEnd =  toStop ? Constants.AUTO_MOVE_MIN_DECEL : Constants.AUTO_INTO_NEXT_MIN_DECEL;
    m_decelerationDistance =  toStop ? Constants.AUTO_MOVE_DECEL_DIST : Constants.AUTO_INTO_NEXT_DECEL_DIST;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_startEncoder = m_driveSubsystem.getTotalPosition();
    m_ramp = new RampInOut(0, m_distance, m_speed,
        m_minAtStart, m_accelerationDistance * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TICS_PER_INCH,
        m_minAtEnd, m_decelerationDistance * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TICS_PER_INCH);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double distanceFromStart = m_driveSubsystem.getTotalPosition() - m_startEncoder;
    m_driveSubsystem.setArcadeSpeed(m_ramp.getValueAtPosition(distanceFromStart) * m_directionMult, 0, true, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setArcadeSpeed(0, 0, true, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double distanceFromStart = m_driveSubsystem.getTotalPosition() - m_startEncoder;
    if (m_directionMult > 0.0) {
      return distanceFromStart > m_distance;
    } else {
      return distanceFromStart < m_distance;
    }
  }
}
