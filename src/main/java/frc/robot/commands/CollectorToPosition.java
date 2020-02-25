package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

// This is a really quick qnd dirty command to hook the sweeper motor to the driver joystic throttle, and the arm position
// to the XBox controller right stick Y for testing. This is completely manual control so the builder/testers can
// move stuff and describe to the programmers what it is they want programmed - i.e. what operations are there and what
// buttons/sticks are they connected to.
public class CollectorToPosition extends CommandBase {

  private final ArmSubsystem m_armSubsystem;
  private final double m_position;
  private final double m_upDeceleration;
  private final double m_downDeceleration;
  private boolean m_direction;

  /**
   * @param armSubsystem
   * @param armPosition
   */
  public CollectorToPosition(ArmSubsystem armSubsystem, Constants.ArmPosition armPosition) {
    m_armSubsystem = armSubsystem;
    m_position = armPosition.POSITION;
    m_upDeceleration = armPosition.UP_DECELERATION;
    m_downDeceleration = armPosition.DOWN_DECELERATION;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(armSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double currentPosition = m_armSubsystem.getPosition();
    if (m_position > currentPosition) {
      m_direction = MoveDirection.UP;
    } else if (m_position < currentPosition) {
      m_direction = MoveDirection.DOWN;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_direction == MoveDirection.UP) {
      m_armSubsystem.setPositionPower(0.5);
    }

    if (m_direction == MoveDirection.DOWN) {
      m_armSubsystem.setPositionPower(-0.5);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armSubsystem.setPositionPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double currentPosition = m_armSubsystem.getPosition();
    if (m_direction == MoveDirection.UP) {
      if (currentPosition >= m_position - m_upDeceleration) {
        return true;
      }
    } else if (m_direction == MoveDirection.DOWN) {
      if (currentPosition <= m_position + m_downDeceleration) {
        return true;
      }
    }

    return false;

  }

  private static class MoveDirection {
    static boolean UP = true;
    static boolean DOWN = false;
  }
}

