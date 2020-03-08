/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.NavX;

public class DriveCommand extends CommandBase {

  private final Joystick m_stick;
  private final DriveSubsystem m_driveSubsystem = DriveSubsystem.getInstance();
  private final NavX m_navx = NavX.getInstance();

  /**
   * Creates a new Drive.
   */
  public DriveCommand(Joystick joyStick) {
    m_stick = joyStick;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // get stick values and set the signs to match the arcade drive forward,rotate conventions
    double stickSpeed = -m_stick.getY();
    double stickTurn = Constants.DRIVER.DRIVE_USE_TWIST ? -m_stick.getTwist() : -m_stick.getX();
    // subtract the dead band and scale what is left outside the dead band
    double speedSignMult = (stickSpeed > 0.0) ? 1.0 : -1.0;
    double turnSignMult = (stickTurn > 0.0) ? 1.0 : -1.0;
    double useSpeed = (Math.abs(stickSpeed) <= Constants.DRIVER.DRIVE_SPEED_DEADBAND) ? 0.0 :
        (Math.abs(stickSpeed) - Constants.DRIVER.DRIVE_SPEED_DEADBAND) / (1.0 - Constants.DRIVER.DRIVE_SPEED_DEADBAND);
    double useTurn = (Math.abs(stickTurn) <= Constants.DRIVER.DRIVE_TURN_DEADBAND) ? 0.0 :
        (Math.abs(stickTurn) - Constants.DRIVER.DRIVE_TURN_DEADBAND) / (1.0 - Constants.DRIVER.DRIVE_TURN_DEADBAND);
    // do the sensitivity power function
    useSpeed = Math.pow(useSpeed, Constants.DRIVER.DRIVE_SPEED_SENSITIVITY);
    useTurn = Math.pow(useTurn, Constants.DRIVER.DRIVE_TURN_SENSITIVITY);
    // apply the gains
    double forward = useSpeed * speedSignMult * Constants.DRIVER.DRIVE_SPEED_GAIN;
    double rotate = useTurn * turnSignMult *
        (Constants.DRIVER.DRIVE_TURN_GAIN +
            (useSpeed * (Constants.DRIVER.DRIVE_TURN_AT_SPEED_GAIN - Constants.DRIVER.DRIVE_TURN_GAIN)));
    // Now set the speeds
    // If the trigger is pressed, track field relative
    if (m_stick.getRawButton(1)) {
      // set expected heading
      int expected;
      int mod = (int) m_navx.getHeadingInfo().heading % 360;
      if ((mod > -90 && mod < 90) || (mod > 270) || (mod < -270)) {
        // forward
        expected = (int) (360 * Math.round(m_navx.getHeadingInfo().heading / 360.0));
      } else {
        // backward
        expected = (int) (180 * Math.round(m_navx.getHeadingInfo().heading / 180.0));
      }
      m_navx.setExpectedHeading(expected);
      // drive with no rotation, tracking expected heading
      m_driveSubsystem.setArcadeSpeed(forward, 0.0, true, false);
    } else {
      m_driveSubsystem.setArcadeSpeed(forward, rotate, 0.0 == rotate, 0.0 != rotate);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
