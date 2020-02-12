/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

  double v;
  double x;
  double y;
  double area;
  String mode;
  String streamMode;

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
    // update vision variables
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tv = table.getEntry("tv"); // whether there are any targets, 0 or 1
    NetworkTableEntry tx = table.getEntry("tx"); // horizontal distance from cursor
    NetworkTableEntry ty = table.getEntry("ty"); // vertical distance from cursor
    NetworkTableEntry ta = table.getEntry("ta"); // area of target

    // read values periodically, not sure why you need getDouble but its in the limelight docs
    v = tv.getDouble(0.0);
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);
  }

  // set modes of the limelight camera
  public void setDriveCamera() {
    table.getEntry("pipeline").setNumber(1); // driver pipeline
    /*
    table.getEntry("ledMode").setNumber(1);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)'
    */
    mode = "drive";
  }

  public void setVisionCamera() {
    table.getEntry("pipeline").setNumber(0); // vision pipeline
    /*
    table.getEntry("ledMode").setNumber(3);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(0);  //1 is driver mode (turns off vision processing)
    */
    mode = "vision";
  }

  public void setSeizureMode() {
    table.getEntry("ledMode").setNumber(2);  //1 is off, 2 is seizure mode, 3 is on
    // table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)
    mode = "seizure";
  }

  public void setLimelightStream() {
    table.getEntry("stream").setNumber(1);
    streamMode = "limelight";
  }

  public void setSideBySideStream() {
    table.getEntry("stream").setNumber(0);
    streamMode = "side by side";
  }

  public void setSecondaryStream() {
    table.getEntry("stream").setNumber(2);
    streamMode = "secondary";
  }

  // return variables, see above
  /*
  public Boolean isTarget() {
    if (v == 0) {return false;}
    else if (v == 1) {return true;}
    else return null;
  }
  */
  public double isTarget() {
    return v;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getArea() {
    return area;
  }

  public String getMode() {
    return mode;
  }

  public NetworkTable getTable() {
    return table;
  }

  public String getStreamMode() {
    return streamMode;
  }
}
