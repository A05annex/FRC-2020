package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.CollectorSubsystem;

// This is a really quick qnd dirty command to hook the sweeper motor to the driver joystic throttle, and the arm position
// to the XBox controller right stick Y for testing. This is completely manual control so the builder/testers can
// move stuff and describe to the programmers what it is they want programmed - i.e. what operations are there and what
// buttons/sticks are they connected to.
public class ManualCollector extends CommandBase {

    private final Joystick m_stick;
    private final XboxController m_xbox;
    private final CollectorSubsystem m_collectorSubsystem;

    /**
     *
     * @param joyStick
     * @param xbox
     * @param collectorSubsystem
     */
    public ManualCollector(CollectorSubsystem collectorSubsystem, Joystick joyStick, XboxController xbox) {
        m_stick = joyStick;
        m_xbox = xbox;
        m_collectorSubsystem = collectorSubsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(collectorSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // What we are doing here is simple - we are setting the sweeper speed to the throttle of the joystick (the only
        // control that lets us easily set and hod a speed), and setting to position motor power based on the setting of the
        // xbox right stick Y
        m_collectorSubsystem.setSweeperPower(m_stick.getThrottle());
        m_collectorSubsystem.setPositionPower(m_xbox.getY(GenericHID.Hand.kRight));
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

