package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;


public class ToggleShift extends CommandBase {

  private final DriveSubsystem m_driveSubsystem;
  private boolean m_isFinished = false;

  public ToggleShift(DriveSubsystem driveSubsystem) {
    m_driveSubsystem = driveSubsystem;
  }

  @Override
  public void initialize() {
    m_isFinished = true;
  }

  @Override
  public void execute() {
    m_driveSubsystem.toggleShift();
    m_isFinished = true;
  }

  @Override
  public boolean isFinished() {
    return m_isFinished;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
