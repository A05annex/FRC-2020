
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerSubsystem;;

public class SpinnerForCounts extends CommandBase {

  private SpinnerSubsystem m_wheel;
  private double m_power;
  private double m_counts;

  /**
   * Run the wheel until it reaches an encoder position.
   */
  public SpinnerForCounts(SpinnerSubsystem wheel, double power, int counts) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_wheel = wheel;
    addRequirements(m_wheel);

    m_power = power;
    m_counts = counts;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_wheel.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_wheel.setPower(m_power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_wheel.setPower(0);
    m_wheel.resetEncoder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_power > 0) {
      if (m_wheel.getEncoder() < m_counts) {
        return true;
      }
        else return false;
    }

    else {
      if (m_wheel.getEncoder() > m_counts) {
        return true;
      }
        else return false;
    }
  }
}
