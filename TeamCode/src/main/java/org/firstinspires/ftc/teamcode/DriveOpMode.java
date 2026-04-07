package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterControlled;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.IMUEx;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Drive w/ Cam")
public class DriveOpMode extends NextFTCOpMode {

    private double xyScale = 1.0;
    private double turnScale = 0.8;

    private TelemetryManager panelsTelemetry;

    public DriveOpMode() {
        addComponents(
                new SubsystemComponent(ShooterControlled.INSTANCE, Feeder.INSTANCE, Intake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    // change the names and directions to suit your robot
    private final MotorEx frontLeftMotor = new MotorEx("frontleft").brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("backleft").brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("frontright").reversed().brakeMode();
    private final MotorEx backRightMotor = new MotorEx("backright").reversed().brakeMode();



    private IMUEx imu = new IMUEx("imu", Direction.DOWN, Direction.LEFT).zeroed();

    @Override
    public void onInit() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        gamepad1.setLedColor(0, 255, 0, 120000);

        telemetry.addLine("Ready");
        telemetry.update();
    }


    @Override
    public void onStartButtonPressed() {
        Command driverControlled  = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate().mapToRange(doubleValue -> doubleValue * xyScale),
                Gamepads.gamepad1().leftStickX().mapToRange(doubleValue -> doubleValue * xyScale),
                Gamepads.gamepad1().rightStickX().mapToRange(doubleValue -> doubleValue * turnScale)
                //, new FieldCentric(imu)
        );

        driverControlled.schedule();

        //INTAKE COMMANDS
        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Intake.INSTANCE.pushOut)
                .whenBecomesTrue(Feeder.INSTANCE.feedOut);

        Gamepads.gamepad1().dpadDown()
                .or(Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue())
                .whenBecomesFalse(Intake.INSTANCE.cutPower)
                .whenBecomesFalse(Feeder.INSTANCE.cutPower);


        Gamepads.gamepad1().dpadDown().not()
                .and(Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue())
                .whenBecomesTrue(Intake.INSTANCE.spinUp)
                .whenBecomesTrue(Feeder.INSTANCE.cutPower);

        Gamepads.gamepad1().share()
                .whenBecomesTrue(new InstantCommand(() -> imu.zeroed()));

        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(Feeder.INSTANCE.spinUp)
                .whenBecomesTrue(ShooterControlled.INSTANCE.spinUp)
                .whenBecomesFalse(Feeder.INSTANCE.cutPower);

        Gamepads.gamepad1().triangle()
                .and(Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue().not())
                .whenBecomesTrue(Intake.INSTANCE.spinUp)
                .whenBecomesFalse(Intake.INSTANCE.cutPower);

        Gamepads.gamepad1().cross()
                .whenBecomesTrue(ShooterControlled.INSTANCE.spinUp);

        Gamepads.gamepad1().circle()
                .whenBecomesTrue(ShooterControlled.INSTANCE.cutPower);
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();
    }
}