/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerSolenoid;

public class SpinnerUpDown extends CommandBase {

  public class Position {
    public static final boolean DOWN = false;
    public static final boolean UP = true;
  }

  private final boolean m_position;
  private final SpinnerSolenoid m_spinnerSolenoid;

  /**
   * Creates a new SpinnerUpDown.
   */
  public SpinnerUpDown(SpinnerSolenoid spinnerSolenoid, boolean position) {
    m_position = position;
    m_spinnerSolenoid = spinnerSolenoid;
    addRequirements(spinnerSolenoid);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_position == Position.UP) {
      m_spinnerSolenoid.spinner_up();
    } else {
      m_spinnerSolenoid.spinner_down();
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
}

