
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerSubsystem;

public class SpinnerForCounts extends CommandBase {

  private SpinnerSubsystem m_spinnerSubsystem = SpinnerSubsystem.getInstance();
  private double m_power;
  private double m_counts;

  /**
   * Run the wheel until it reaches an encoder position.
   */
  public SpinnerForCounts(double power, int counts) {
    m_power = power;
    m_counts = counts;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_spinnerSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_spinnerSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_spinnerSubsystem.setPower(m_power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_spinnerSubsystem.setPower(0);
    m_spinnerSubsystem.resetEncoder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_power > 0) {
      if (m_spinnerSubsystem.getEncoder() < m_counts) {
        return true;
      }
      return false;
    } else {
      if (m_spinnerSubsystem.getEncoder() > m_counts) {
        return true;
      }
      return false;
    }
  }
}
