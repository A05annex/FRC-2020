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

public class AutoDrive extends CommandBase {

  private final DriveSubsystem m_driveSubsystem;
  private double m_distance;
  private double m_speed;
  private double startEncoder;
  private boolean forward;
  private RampInOut ramp;
  private double distanceFromStart;
  private double directionMult;

  /**
   * Drives straight forwards for a certian amount of inches, at a certian speed.
   *
   * @param driveSubsystem   The drive subsystem.
   * @param distanceInInches Distance to travel in inches.
   * @param speed            Speed from 1 to 0. Do not make this negative!
   */
  public AutoDrive(DriveSubsystem driveSubsystem, double distanceInInches, double speed) {
    m_driveSubsystem = driveSubsystem;
    m_distance = distanceInInches * Constants.ROBOT.DRIVE_TICS_PER_INCH;
    m_speed = Math.abs(speed);
    // reverse speed if distance is negative
    if (distanceInInches < 0) {
      forward = false;
      directionMult = -1;
    } else {
      forward = true;
      directionMult = 1;
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startEncoder = m_driveSubsystem.getTotalPosition();
    ramp = new RampInOut(0, m_distance, m_speed, .15, 30 * Constants.ROBOT.DRIVE_TICS_PER_INCH, .15, 40 * Constants.ROBOT.DRIVE_TICS_PER_INCH);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    distanceFromStart = m_driveSubsystem.getTotalPosition() - startEncoder;
    m_driveSubsystem.setArcadeSpeed(ramp.getValueAtPosition(distanceFromStart) * directionMult, 0, true, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setArcadeSpeed(0, 0, true, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    distanceFromStart = m_driveSubsystem.getTotalPosition() - startEncoder;
    if (forward) {
      if (distanceFromStart > m_distance) {
        return true;
      } else {
        return false;
      }
    } else {
      if (distanceFromStart < m_distance) {
        return true;
      } else {
        return false;
      }
    }
  }
}

