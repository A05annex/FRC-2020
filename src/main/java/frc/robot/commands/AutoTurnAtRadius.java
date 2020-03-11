package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.NavX;
import frc.robot.RampInOut;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This is a command that moves the robot forward while turning the specified number of degrees at the specified radius
 */
public class AutoTurnAtRadius extends CommandBase {

  // the expected overshoot at speed 1
  static private final double EXPECTED_OVERSHOOT = 10.0;
  // the width of the drive (wheel center to wheel center
  static final double DRIVE_WIDTH = 36.0;

  private final DriveSubsystem m_driveSubsystem = DriveSubsystem.getInstance();
  private NavX m_navx = NavX.getInstance();
  private double m_speed;
  private double m_degrees;
  private double m_radius;
  private double m_minAtStart = Constants.AUTO_AT_RADIUS_MIN_ACCEL;
  private double m_accelerationDistance = Constants.AUTO_AT_RADIUS_ACCEL_DIST;
  private double m_minAtEnd = Constants.AUTO_AT_RADIUS_MIN_DECEL;
  private double m_decelerationDistance = Constants.AUTO_AT_RADIUS_DECEL_DIST;
  private boolean m_clockwise;
  private RampInOut m_ramp;

  public AutoTurnAtRadius(double radiusInInches, double degrees) {
    this(radiusInInches, degrees, Constants.AUTO_TURN_AT_RADIUS);
  }

  public AutoTurnAtRadius(double radiusInInches, double degrees, double speed) {
    m_radius = radiusInInches;
    m_degrees = degrees;
    m_speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  public AutoTurnAtRadius(double radiusInInches, double degrees, double speed, double minAtStart,
                   double accelerationDistance, double minAtEnd, double decelerationDistance) {
    this(radiusInInches, degrees, speed);
    m_minAtStart = minAtStart;
    m_accelerationDistance = accelerationDistance;
    m_minAtEnd = minAtEnd;
    m_decelerationDistance = decelerationDistance;
  }

  /**
   * Turns at radius forward/backward for a specified number of degrees, at a specified maximum speed, using the default
   * accelerations and decelerations depending on whether the move starts/stops at rest, or, is the continuation of a
   * previous move.
   *
   * @param distanceInInches (double) Degrees to travel in inches, positive is clockwise, negative is counter-clockwise.
   * @param fromStop (boolean) {@code true} if this move is starting from a stopped position, {@code false} if this is
   *                 the continuation of another move or turn at radius.
   * @param toStop (boolean) {@code true} if this move is ending in a stopped position, {@code false} if there is
   *               another move or turn at radius following this.
   */
  public AutoTurnAtRadius(double distanceInInches, double speed, boolean fromStop, boolean toStop) {
    this(distanceInInches, speed);
    m_minAtStart = fromStop ? Constants.AUTO_MOVE_MIN_ACCEL : Constants.AUTO_INTO_NEXT_MIN_ACCEL;
    m_accelerationDistance = fromStop ? Constants.AUTO_MOVE_ACCEL_DIST : Constants.AUTO_INTO_NEXT_ACCEL_DIST;
    m_minAtEnd =  toStop ? Constants.AUTO_MOVE_MIN_DECEL : Constants.AUTO_INTO_NEXT_MIN_DECEL;
    m_decelerationDistance =  toStop ? Constants.AUTO_MOVE_DECEL_DIST : Constants.AUTO_INTO_NEXT_DECEL_DIST;
  }

  @Override
  public void initialize() {
    m_navx.incrementExpectedHeading(m_degrees);
    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    double actualDegrees = (headingInfo.expectedHeading - headingInfo.heading);
    m_clockwise = actualDegrees > 0.0;
    m_ramp = new RampInOut(headingInfo.heading, headingInfo.expectedHeading, m_speed,
        m_minAtStart, m_accelerationDistance, m_minAtEnd, m_decelerationDistance);
  }

  @Override
  public void execute() {
    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    double speed = m_ramp.getValueAtPosition(headingInfo.heading);
    double fasterSide = (m_radius < 1) ?
        speed : ((m_radius + (DRIVE_WIDTH * 0.5)) / m_radius) * speed;
    double slowerSide = (m_radius < 1) ?
        -speed : ((m_radius - (DRIVE_WIDTH * 0.5)) / m_radius) * speed;
    m_driveSubsystem.setTankSpeed(
        (m_clockwise && (speed > 0.0)) ? slowerSide : fasterSide,
        (m_clockwise && (speed > 0.0)) ? fasterSide : slowerSide);
  }

  @Override
  public boolean isFinished() {
    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    if (m_clockwise) {
      if (headingInfo.heading + EXPECTED_OVERSHOOT >= headingInfo.expectedHeading) {
        return true;
      } else {
        return false;
      }
    } else {
      if (headingInfo.heading - EXPECTED_OVERSHOOT <= headingInfo.expectedHeading) {
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
  }
}
