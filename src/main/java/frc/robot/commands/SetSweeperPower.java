/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerSubsystem;
import frc.robot.subsystems.SweeperSubsystem;

public class SetSweeperPower extends CommandBase {

  private final SweeperSubsystem m_sweeperSubsystem = SweeperSubsystem.getInstance();
  private final SpinnerSubsystem m_spinnerSubsystem = SpinnerSubsystem.getInstance();
  private double m_power;

  /**
   * Creates a new SetSweeperPower.
   */
  public SetSweeperPower(double power) {
    m_power = power;
    addRequirements(m_sweeperSubsystem);
    addRequirements(m_spinnerSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_sweeperSubsystem.setSweeperPower(m_power);
    m_spinnerSubsystem.setPower(-m_power);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
