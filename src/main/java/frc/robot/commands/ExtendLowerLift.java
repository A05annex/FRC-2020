/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LowerCylinderSubsystem;

public class ExtendLowerLift extends CommandBase {

  private final LowerCylinderSubsystem m_lowerCylinderSubsystem;
  private boolean m_isFinished = false;

  /**
   * Creates a new ExtendLowerLift.
   */
  public ExtendLowerLift(LowerCylinderSubsystem lowerCylinderSubsystem) {
    m_lowerCylinderSubsystem = lowerCylinderSubsystem;
    addRequirements(m_lowerCylinderSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_lowerCylinderSubsystem.extendCylinders();
    m_isFinished = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
