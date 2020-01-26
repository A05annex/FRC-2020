/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;

public class controlPanelBigWheel extends SubsystemBase {

  public TalonSRX bigWheel = new TalonSRX(Constants.MotorControllers.BIG_WHEEL);

  /**
   * Creates a new controlPanelBigWheel.
   */
  public controlPanelBigWheel() {
    
    bigWheel.configFactoryDefault();
    bigWheel.setNeutralMode(NeutralMode.Brake);
    bigWheel.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double power) {
    bigWheel.set(ControlMode.PercentOutput, power);
  }

}