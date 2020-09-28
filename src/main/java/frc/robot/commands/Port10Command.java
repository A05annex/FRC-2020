package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Port10Subsystem;


public class Port10Command extends CommandBase {

  private final Port10Subsystem m_Port10Subsystem = Port10Subsystem.getInstance();

  public Port10Command() {
    addRequirements(m_Port10Subsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_Port10Subsystem.setPower(Constants.PORT10_SPEED);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
