/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SpinnerSubsystem extends SubsystemBase {

  public final TalonSRX m_spinner = new TalonSRX(Constants.MotorControllers.SPINNER);
  private double m_lastPower = 0.0;

  /**
   * Creates a new SpinnerSubsystem.
   */
  private SpinnerSubsystem() {

    m_spinner.configFactoryDefault();
    m_spinner.setNeutralMode(NeutralMode.Brake);
    m_spinner.set(ControlMode.PercentOutput, 0.0);
    m_spinner.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    resetEncoder();

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double power)
  {
    if (power != m_lastPower) {
      m_spinner.set(ControlMode.PercentOutput, power);
      m_lastPower = power;
    }
  }

  public int getEncoder() {
    return m_spinner.getSelectedSensorPosition();
  }

  public void resetEncoder() {
    m_spinner.setSelectedSensorPosition(0);
  }

  //================================================================================================================================
  /**
   * The Singleton instance of this SpinnerSubsystem. External classes should
   * use the {@link #SpinnerSubsystem()} method to get the instance.
   */
  private static final SpinnerSubsystem INSTANCE = new SpinnerSubsystem();

  /**
   * Returns the Singleton instance of this SpinnerSubsystem. This static method
   * should be used -- {@code SpinnerSubsystem.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static SpinnerSubsystem getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}
