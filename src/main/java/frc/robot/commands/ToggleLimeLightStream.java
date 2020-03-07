package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;


public class ToggleLimeLightStream extends CommandBase {

  Limelight m_limelight = Limelight.getInstance();
  boolean m_isFinished;

  public ToggleLimeLightStream() {
    addRequirements(m_limelight);
  }

  @Override
  public void initialize() {
    m_isFinished = false;
  }

  @Override
  public void execute() {
    m_limelight.toggleStream();
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
