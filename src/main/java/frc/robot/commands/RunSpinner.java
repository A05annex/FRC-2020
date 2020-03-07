package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SpinnerSubsystem;


public class RunSpinner extends CommandBase {

  private final XboxController m_xbox;
  private final SpinnerSubsystem m_spinnerSubsystem = SpinnerSubsystem.getInstance();

  public RunSpinner(XboxController xbox) {
    m_xbox = xbox;
    addRequirements(m_spinnerSubsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    // so right now we an xbox triggers for left or right spin. pick the biggest absolute as
    // the one to listen to - if there is no biggest, don't spin.
    double left = m_xbox.getTriggerAxis(GenericHID.Hand.kLeft);
    double right = -m_xbox.getTriggerAxis(GenericHID.Hand.kRight);
    double absLeft = Math.abs(left);
    double absRight = Math.abs(right);
    if ((absLeft > absRight) && (absLeft > 0.05)) {
      m_spinnerSubsystem.setPower(left);
    } else if ((absLeft < absRight) && (absRight > 0.05)) {
      m_spinnerSubsystem.setPower(right);
    } else {
      m_spinnerSubsystem.setPower(-m_xbox.getY(GenericHID.Hand.kRight));
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
