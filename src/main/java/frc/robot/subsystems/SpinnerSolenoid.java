/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class SpinnerSolenoid extends SubsystemBase {
  /**
   * Creates a new SpinnerSolenoid.
   */
  private final Solenoid m_spinnerSolenoid = new Solenoid(Constants.Pneumatics.SPINNER_LIFT);

  public SpinnerSolenoid() {
    m_spinnerSolenoid.set(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  public void spinner_up() {
    m_spinnerSolenoid.set(true);
  }

  /**
   * Lowers spinner after spinning wheel so we can countinue driving.
   */
  public void spinner_down() {
    m_spinnerSolenoid.set(false);
  }

}

