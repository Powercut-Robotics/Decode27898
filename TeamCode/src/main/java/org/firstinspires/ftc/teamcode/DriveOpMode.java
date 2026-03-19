package org.firstinspires.ftc.teamcode;

import android.icu.text.TimeZoneFormat;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Camera;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterControlled;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.PedroComponent;
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

    private double xyScale = 0.7;
    private double turnScale = 0.5;

    private TelemetryManager panelsTelemetry;

    public DriveOpMode() {
        addComponents(
                new SubsystemComponent(ShooterControlled.INSTANCE, Feeder.INSTANCE, Intake.INSTANCE, Camera.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    // change the names and directions to suit your robot
    private final MotorEx frontLeftMotor = new MotorEx("frontLeftMotor").reversed().brakeMode();
    private final MotorEx backLeftMotor = new MotorEx("backLeftMotor").reversed().brakeMode();
    private final MotorEx frontRightMotor = new MotorEx("frontRightMotor").brakeMode();
    private final MotorEx backRightMotor = new MotorEx("backRightMotor").reversed().brakeMode();
    private IMUEx imu = new IMUEx("imu", Direction.LEFT, Direction.UP).zeroed();

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
                Gamepads.gamepad1().rightStickX().mapToRange(doubleValue -> doubleValue * turnScale),
                new FieldCentric(imu)
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

        Gamepads.gamepad1().touchpad()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(ShooterControlled.INSTANCE.sensorPanic)
                .whenBecomesTrue(new InstantCommand(() -> gamepad1.setLedColor(255, 0, 0, 120000)))
                .whenBecomesFalse(ShooterControlled.INSTANCE.sensorUnPanic)
                .whenBecomesFalse(new InstantCommand(() -> gamepad1.setLedColor(0, 255, 0, 120000)));


        Gamepads.gamepad1().share()
                .whenBecomesTrue(new InstantCommand(() -> imu.zeroed()));


        //DELETE SECTION IF ISSUES SHOOTING W/O HOLDING TRIANGLE
            //feeder when intaking
            ShooterControlled.INSTANCE.ballDetected.not()
                    .and(Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue())
                    .whenTrue(Feeder.INSTANCE.spinUp);

        ShooterControlled.INSTANCE.ballDetected.not()
                .and(Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue().not())
                .whenBecomesTrue(Feeder.INSTANCE.cutPower);

            //if we detect ball And Not holdining triangle, cut feeder
            ShooterControlled.INSTANCE.ballDetected
                    .and(Gamepads.gamepad1().triangle().not())
                    .whenBecomesTrue(Feeder.INSTANCE.cutPower);
        //

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