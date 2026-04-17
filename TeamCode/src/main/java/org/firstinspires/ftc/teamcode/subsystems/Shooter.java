package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class Shooter implements Subsystem {
    public static final Shooter INSTANCE = new Shooter();
    private TelemetryManager telemetry;
    private Shooter() {

    }


    //Flywheel control system
    public double velocity = 0;

    public static double velocityTargetLow = 1175;
    public static double velocityTargetMid = 1250;
    public static double velocityTargetHigh = 1375;

    //Full battery required

    public static PIDCoefficients flywheelPIDCoef = new PIDCoefficients(-0.001, 0.0, 0.0);
    public static BasicFeedforwardParameters flywheelFFCoef = new BasicFeedforwardParameters(-0.0005, 0, -0.0275);

    private static final ControlSystem flywheelControlSystem = ControlSystem.builder()
            .velPid(flywheelPIDCoef)
            .basicFF(flywheelFFCoef)
            .build();


    private final MotorEx flywheelMotor = new MotorEx("shooter motor")
            .floatMode()
            .reversed();

    private final MotorEx flywheelMotorTwo = new MotorEx("shooter motor2")
            .floatMode()
            .reversed();



    //Flywheel commands
    public Command spinUpLow = new InstantCommand(() -> flywheelControlSystem.setGoal(new KineticState(0.0, velocityTargetLow))).requires(this);
    public Command spinUpMid = new InstantCommand(() -> flywheelControlSystem.setGoal(new KineticState(0.0, velocityTargetMid))).requires(this);
    public Command spinUpHigh = new InstantCommand(() -> flywheelControlSystem.setGoal(new KineticState(0.0, velocityTargetHigh))).requires(this);


    public Command cutPower = new InstantCommand(() -> flywheelControlSystem.setGoal(new KineticState(0.0, 0.0))).requires(this);


    //ball




    @Override
    public void initialize() {
        telemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        flywheelControlSystem.setGoal(new KineticState(0.0, 0.0));
    }

    @Override
    public void periodic() {
        if (ActiveOpMode.isStarted()) {
            velocity = flywheelMotor.getVelocity();

            double power = flywheelControlSystem.calculate(new KineticState(
                    flywheelMotor.getCurrentPosition(),
                    flywheelMotor.getVelocity())
            );


            flywheelMotor.setPower(power);
            flywheelMotorTwo.setPower(power);

            telemetry.addData("Flywheel Velocity", velocity);
        }
    }

}
