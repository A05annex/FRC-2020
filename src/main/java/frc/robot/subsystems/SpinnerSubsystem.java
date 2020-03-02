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

  public TalonSRX bigWheel = new TalonSRX(Constants.MotorControllers.SPINNER);

  /**
   * Creates a new SpinnerSubsystem.
   */
  private SpinnerSubsystem() {

    bigWheel.configFactoryDefault();
    bigWheel.setNeutralMode(NeutralMode.Brake);
    bigWheel.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    resetEncoder();

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double power) {
    bigWheel.set(ControlMode.PercentOutput, power);
  }

  public int getEncoder() {
    return bigWheel.getSelectedSensorPosition();
  }

  public void resetEncoder() {
    bigWheel.setSelectedSensorPosition(0);
  }

}
