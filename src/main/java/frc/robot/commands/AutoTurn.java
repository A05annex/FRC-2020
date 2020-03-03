/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.NavX;
import frc.robot.RampInOut;
import frc.robot.subsystems.DriveSubsystem;

public class AutoTurn extends CommandBase {

  static private final double EXPECTED_OVERSHOOT = 0.0;

  private final DriveSubsystem m_driveSubsystem = DriveSubsystem.getInstance();
  private final NavX m_navx = NavX.getInstance();
  private double m_speed;
  private double m_degrees;
  private boolean m_clockwise;
  private double m_directionMult;
  private RampInOut m_ramp;


  /**
   * Turns for a certain amount of degrees, at a certain speed.
   * @param degrees        Amount of degrees to turn positive is clockwise
   * @param speed          Speed from 1 to 0. Do not make this negative!
   */
  public AutoTurn(double degrees, double speed) {
    m_degrees = degrees;
    m_speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_navx.incrementExpectedHeading(m_degrees);
    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    double actualDegrees = (headingInfo.expectedHeading - headingInfo.heading);
    // reverse speed if degrees are negative
    if (actualDegrees < 0) {
      m_clockwise = false;
      m_directionMult = 1;
    } else {
      m_clockwise = true;
      m_directionMult = -1;
    }
    m_ramp = new RampInOut(headingInfo.heading, headingInfo.expectedHeading, m_speed,
        .25, 20, .10, 50);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
    m_driveSubsystem.setArcadeSpeed(0, m_ramp.getValueAtPosition(headingInfo.heading) * m_directionMult,
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
}
