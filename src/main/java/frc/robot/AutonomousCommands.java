package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The autonomous Command Programs
 */
public enum AutonomousCommands {
  POSITION1("center", null),
  POSITION2("right", null),
  POSITION3("left", null),
  POSITION4("full", null);


  public final String NAME;
  public final Command COMMAND;

  AutonomousCommands(String name, Command command) {
    NAME = name;
    COMMAND = command;
  }

  static public String[] asStringArray() {
    AutonomousCommands autos[] = AutonomousCommands.values();
    String[] strings = new String[autos.length];
    for (int i = 0; i < autos.length; i++) {
      strings[i] = autos[i].NAME;
    }
    return strings;
  }

  static public Command getDefaultCommand() {
    return POSITION1.COMMAND;
  }

  static public String getDefaultName() {
    return POSITION1.NAME;
  }
}
