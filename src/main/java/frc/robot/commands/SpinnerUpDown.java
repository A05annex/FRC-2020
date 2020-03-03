/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerLift;

public class SpinnerUpDown extends CommandBase {

  private final boolean m_position;
  private final SpinnerLift m_spinnerLift = SpinnerLift.getInstance();

  /**
   * Creates a new SpinnerUpDown.
   */
  public SpinnerUpDown(boolean position) {
    m_position = position;
    addRequirements(m_spinnerLift);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_position == Position.UP) {
      m_spinnerLift.spinner_up();
    } else {
      m_spinnerLift.spinner_down();
    }
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

  public class Position {
    public static final boolean DOWN = false;
    public static final boolean UP = true;
  }
}

