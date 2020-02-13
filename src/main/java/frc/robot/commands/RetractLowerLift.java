package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftSubsystem;


public class RetractLowerLift extends CommandBase {

  private final LiftSubsystem m_liftSubsystem;
  private boolean m_isFinished = false;

  public RetractLowerLift(LiftSubsystem liftSubsystem) {
    m_liftSubsystem = liftSubsystem;
    addRequirements(m_liftSubsystem);
  }

  @Override
  public void initialize() {
    m_isFinished = false;
  }

  @Override
  public void execute() {
    m_liftSubsystem.retractLower();
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
