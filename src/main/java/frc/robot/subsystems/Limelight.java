/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

  /**
   * Creates a new Limelight.
   */
  public Limelight() {
    // when initialized, use driver camera
    setDriveCamera();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setDriveCamera() {
    table.getEntry("ledMode").setNumber(1);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)
  }

  public void setVisionCamera() {
    table.getEntry("ledMode").setNumber(3);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(0);  //1 is driver mode (turns off vision processing)
  }
}
