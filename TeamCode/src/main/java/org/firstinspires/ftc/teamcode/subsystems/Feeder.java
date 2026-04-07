package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

@Configurable
public class Feeder implements Subsystem {

    public static final Feeder INSTANCE = new Feeder();
    private TelemetryManager telemetry;
    private Feeder() {}

    private double power = 0;
    private double angle = 0;
    private static double targetPower = 0.7;


    private final MotorEx pusherMotor = new MotorEx("loader motor")
            .brakeMode();


    public Command spinUp = new InstantCommand(() -> power = targetPower).requires(this);
    public Command feedOut = new InstantCommand(() -> power = -0.5).requires(this);

    public Command cutPower = new InstantCommand(() -> power = 0).requires(this);
    @Override
    public void initialize() {
        telemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    }


    @Override
    public void periodic() {

            pusherMotor.setPower(power);
    }
}
