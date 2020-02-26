/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.NavX;
import frc.robot.RampInOut;
import frc.robot.subsystems.DriveSubsystem;

public class AutoTurn extends CommandBase {

  static private final double EXPECTED_OVERSHOOT = 0.0;

  private final DriveSubsystem m_driveSubsystem;
  private double m_difference;
  private double m_speed;
  private double m_degrees;
  private double m_startHeading;
  private boolean clockwise;
  private double directionMult;
  private double startDifference;
  private RampInOut ramp;
  private double differenceDifference;


  /**
   * Turns for a certain amount of degrees, at a certain speed.
   *
   * @param driveSubsystem The drive subsystem.
   * @param degrees        Amount of degrees to turn positive is clockwise
   * @param speed          Speed from 1 to 0. Do not make this negative!
   */
  public AutoTurn(DriveSubsystem driveSubsystem, double degrees, double speed) {
    m_driveSubsystem = driveSubsystem;
    m_degrees = degrees;
    m_speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    NavX.getInstance().incrementExpectedHeading(m_degrees);
    NavX.HeadingInfo headingInfo = NavX.getInstance().getHeadingInfo();
    m_startHeading = headingInfo.heading;
    double actualDegrees = (headingInfo.expectedHeading - headingInfo.heading);
    m_difference = actualDegrees * Constants.ROBOT.DRIVE_TICS_PER_DEGREE;
    // reverse speed if degrees are negative
    if (actualDegrees < 0) {
      clockwise = false;
      directionMult = 1;
    } else {
      clockwise = true;
      directionMult = -1;
    }
    startDifference = m_driveSubsystem.getDifferencePosition();
    ramp = new RampInOut(m_startHeading, headingInfo.expectedHeading, m_speed,
        .25, 30, .10, 50);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    NavX.HeadingInfo headingInfo = NavX.getInstance().getHeadingInfo();
    m_driveSubsystem.setArcadeSpeed(0, ramp.getValueAtPosition(headingInfo.heading) * directionMult,
        false, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setArcadeSpeed(0, 0, false, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    NavX.HeadingInfo headingInfo = NavX.getInstance().getHeadingInfo();
    if (clockwise) {
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
}