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


public class SpinnerLift extends SubsystemBase {

  private final Solenoid m_spinnerSolenoid = new Solenoid(Constants.Pneumatics.SPINNER_LIFT);

  /**
   * Creates a new SpinnerSolenoid.
   * This constructor is private since this class is a Singleton. External classes
   * should use the {@link #getInstance()} method to get the instance.
   */
  private SpinnerLift() {
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
   * Lowers spinner after spinning wheel so we can continue driving.
   */
  public void spinner_down() {
    m_spinnerSolenoid.set(false);
  }

  //================================================================================================================================
  /**
   * The Singleton instance of this SpinnerSolenoid. External classes should
   * use the {@link #getInstance()} method to get the instance.
   */
  private static final SpinnerLift INSTANCE = new SpinnerLift();

  /**
   * Returns the Singleton instance of this SpinnerSolenoid. This static method
   * should be used -- {@code SpinnerSolenoid.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static SpinnerLift getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}


