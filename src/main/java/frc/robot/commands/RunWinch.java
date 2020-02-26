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
    m_liftSubsystem.dumpLiftPressure();
    m_liftSubsystem.setWinchPower(m_power);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    m_liftSubsystem.setWinchPower(0.0);
  }
}
