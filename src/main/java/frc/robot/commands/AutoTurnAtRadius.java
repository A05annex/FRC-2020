package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.NavX;
import frc.robot.RampInOut;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This is a command that moves the robot forward while turning the specified number of degrees at the specified radius
 */
public class AutoTurnAtRadius extends CommandBase {

  // the expected overshoot at speed 1
  static private final double EXPECTED_OVERSHOOT = 0.0;
  // the width of the drive (wheel center to wheel center
  static final double DRIVE_WIDTH = 24.0;

  private final DriveSubsystem m_driveSubsystem;
  private NavX m_navx = NavX.getInstance();
  private double m_speed;
  private double m_degrees;
  private double m_radius;
  private double m_minAtStart = .20;
  private double m_accelerationDistance = 20;
  private double m_minAtEnd = .15;
  private double m_decelerationDistance = 30;
  private boolean m_clockwise;
  private RampInOut m_ramp;

  public AutoTurnAtRadius(DriveSubsystem driveSubsystem, double radiusInInches, double degrees, double speed) {
    m_driveSubsystem = driveSubsystem;
    m_radius = radiusInInches;
    m_degrees = degrees;
    m_speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  public AutoTurnAtRadius(DriveSubsystem driveSubsystem, double radiusInInches, double degrees, double speed, double minAtStart,
                   double accelerationDistance, double minAtEnd, double decelerationDistance) {
    this(driveSubsystem, radiusInInches, degrees, speed);
    m_minAtStart = minAtStart;
    m_accelerationDistance = accelerationDistance;
    m_minAtEnd = minAtEnd;
    m_decelerationDistance = decelerationDistance;
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
    double fasterSide = ((m_radius + (DRIVE_WIDTH * 0.5)) / m_radius) * speed;
    double slowerSide = ((m_radius - (DRIVE_WIDTH * 0.5)) / m_radius) * speed;
    m_driveSubsystem.sepTankSpeed(m_clockwise ? slowerSide : fasterSide, m_clockwise ? fasterSide : slowerSide);
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
