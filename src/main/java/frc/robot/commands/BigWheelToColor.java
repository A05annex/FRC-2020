/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.controlPanelBigWheel;

public class BigWheelToColor extends CommandBase {

  private controlPanelBigWheel m_wheel;
  private double m_power;
  private String m_colorString;
  private String m_targetColor;

  /**
   * Spins the wheel until it sees a specific color.
   * @param wheel The wheel subsystem.
   * @param power (double) Power to run the wheel at from 0 to 1.
   * @param targetColor (String) "Blue", "Green", "Red", or "Yellow".
   */
  public BigWheelToColor(controlPanelBigWheel wheel, double power, String targetColor) {
    // Use addRequirements() here to declare subsystem dependencies.\
    m_wheel = wheel;
    addRequirements(m_wheel);

    m_power = power;
    m_targetColor = targetColor;
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
    m_colorString = RobotContainer.getColorAsString();
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
    if (m_colorString == m_targetColor) {
      return true;
    }
    else return false;
  }
}
