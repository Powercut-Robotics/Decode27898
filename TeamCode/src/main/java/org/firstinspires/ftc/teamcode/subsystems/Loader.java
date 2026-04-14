package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

@Configurable
public class Loader implements Subsystem {

    public static final Loader INSTANCE = new Loader();
    private TelemetryManager telemetry;
    private Loader() {}

    private double power = 0;
    private double angle = 0;
    private static double targetPower = 0.7;


    private final MotorEx loaderMotor = new MotorEx("loader motor")
            .brakeMode()
            .reversed();


    public Command spinUp = new InstantCommand(() -> power = targetPower).requires(this);
    public Command feedOut = new InstantCommand(() -> power = -0.5).requires(this);

    public Command cutPower = new InstantCommand(() -> power = 0).requires(this);
    @Override
    public void initialize() {
        telemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    }


    @Override
    public void periodic() {

            loaderMotor.setPower(power);
    }
}
