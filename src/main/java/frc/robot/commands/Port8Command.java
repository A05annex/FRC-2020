package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Port8Subsystem;


public class Port8Command extends CommandBase {

  private final Port8Subsystem m_Port8Subsystem = Port8Subsystem.getInstance();

  public Port8Command() {
    addRequirements(m_Port8Subsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_Port8Subsystem.setPower(Constants.PORT8_SPEED);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
