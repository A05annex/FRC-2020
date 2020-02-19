/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;

public class AutoTurn extends CommandBase {

  private final DriveSubsystem m_driveSubsystem;
  private double m_difference;
  private double m_speed;
  private double startDifference;
  private boolean clockwise;

  /**
   * Turns for a certain amount of degrees, at a certian speed.
   * @param driveSubsystem The drive subsystem.
   * @param degrees Amount of degrees to turn.
   * @param speed Speed from 1 to 0. Do not make this negative!
   */
  public AutoTurn(DriveSubsystem driveSubsystem, double degrees, double speed) {
    m_driveSubsystem = driveSubsystem;
    m_difference = degrees * Constants.ROBOT.DRIVE_TICS_PER_DEGREE;
    m_speed = Math.abs(speed);
    // reverse speed if degrees are negative
    if (degrees < 0) {
      clockwise = false;
    } else {
      m_speed *= -1;
      clockwise = true;
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startDifference = m_driveSubsystem.getDifferencePosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveSubsystem.setArcadeSpeed(0, m_speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setArcadeSpeed(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double differenceDifference = m_driveSubsystem.getDifferencePosition() - startDifference;
    if (clockwise) {
      if (differenceDifference > m_difference) {
        return true;
      } else {
        return false;
      }
    } else {
      if (differenceDifference < m_difference) {
        return true;
      } else {
        return false;
      }
    }
  }
}
