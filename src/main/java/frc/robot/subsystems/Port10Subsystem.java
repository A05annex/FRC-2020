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

public class Port10Subsystem extends SubsystemBase {

  public final TalonSRX m_motor = new TalonSRX(Constants.MotorControllers.PORT10_MOTOR);
  private double m_lastPower = 0.0;
  /**
   * Creates a new Port10Subsystem.
   */
  private Port10Subsystem() {

    m_motor.configFactoryDefault();
    m_motor.setNeutralMode(NeutralMode.Brake);
    m_motor.set(ControlMode.PercentOutput, 0.0);
    m_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    resetEncoder();

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double power) {
    if (power != m_lastPower) {
      m_motor.set(ControlMode.PercentOutput, power);
      m_lastPower = power;
    }
  }

  public int getEncoder() {
    return m_motor.getSelectedSensorPosition();
  }

  public int getEncoderVel() {
    return m_motor.getSelectedSensorVelocity();
  }

  public void resetEncoder() {
    m_motor.setSelectedSensorPosition(0);
  }

  //================================================================================================================================
  /**
   * The Singleton instance of this Port10Subsystem. External classes should
   * use the {@link #Port10Subsystem()} method to get the instance.
   */
  private static final Port10Subsystem INSTANCE = new Port10Subsystem();

  /**
   * Returns the Singleton instance of this Port10Subsystem. This static method
   * should be used -- {@code Port10Subsystem.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static Port10Subsystem getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}
