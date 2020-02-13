package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftSubsystem;


public class RunWinch extends CommandBase {

  private final LiftSubsystem m_liftSubsystem;
  private final double m_power;

  public RunWinch(LiftSubsystem liftSubsystem, double power) {
    m_liftSubsystem = liftSubsystem;
    m_power = power;
    addRequirements(m_liftSubsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_liftSubsystem.setWinchPower(m_power);
  }

  @Override
  public boolean isFinished() {
    // TODO: Make this return true when this Command no longer needs to run execute()
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    m_liftSubsystem.setWinchPower(0.0);
  }
}
