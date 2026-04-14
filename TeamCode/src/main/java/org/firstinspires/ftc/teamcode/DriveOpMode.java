package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Loader;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

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

@TeleOp(name = "Drive")
public class DriveOpMode extends NextFTCOpMode {

    private double xyScale = 0.75;
    private double turnScale = 0.75;

    private TelemetryManager panelsTelemetry;

    public DriveOpMode() {
        addComponents(
                new SubsystemComponent(Shooter.INSTANCE, Loader.INSTANCE, Intake.INSTANCE),
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
                , new FieldCentric(imu)
        );

        driverControlled.schedule();
        Shooter.INSTANCE.spinUpMid.schedule();

        //Drive scales
//        Gamepads.gamepad1().rightBumper()
//                .whenBecomesTrue(new InstantCommand(() -> xyScale = 0.25))
//                .whenBecomesTrue(new InstantCommand(() -> turnScale = 0.25))
//                .whenBecomesTrue(new InstantCommand(() -> Gamepads.gamepad1().getGamepad().invoke().setLedColor(255, 0, 0, 120000)));
//
//        Gamepads.gamepad1().leftBumper()
//                .whenBecomesTrue(new InstantCommand(() -> xyScale = 1))
//                .whenBecomesTrue(new InstantCommand(() -> turnScale = 1))
//                .whenBecomesTrue(new InstantCommand(() -> Gamepads.gamepad1().getGamepad().invoke().setLedColor(0, 0, 255, 120000)));

        Gamepads.gamepad1().leftBumper()
                .or(Gamepads.gamepad1().rightBumper())
                    .whenFalse(new InstantCommand(() -> xyScale = 0.75))
                    .whenFalse(new InstantCommand(() -> xyScale = 0.75))
                    .whenBecomesFalse(new InstantCommand(() -> Gamepads.gamepad1().getGamepad().invoke().setLedColor(0, 255, 0, 120000)));

        Gamepads.gamepad1().share()
                .whenBecomesTrue(new InstantCommand(() -> imu.zeroed()))
                .whenBecomesTrue(new InstantCommand(() -> Gamepads.gamepad1().getGamepad().invoke().rumble(250)));
        //INTAKE COMMANDS

        Gamepads.gamepad1().dpadUp()
                .whenBecomesTrue(Shooter.INSTANCE.spinUpHigh);

        Gamepads.gamepad1().dpadRight()
                .whenBecomesTrue(Shooter.INSTANCE.spinUpMid);

        Gamepads.gamepad1().dpadDown()
                .whenBecomesTrue(Shooter.INSTANCE.spinUpLow);

        Gamepads.gamepad1().dpadLeft()
                .whenBecomesTrue(Shooter.INSTANCE.cutPower);


        Gamepads.gamepad1().rightBumper()
                .whenTrue(Intake.INSTANCE.spinUp)
                .whenBecomesFalse(Intake.INSTANCE.cutPower);

        Gamepads.gamepad1().square()
                .whenTrue(Loader.INSTANCE.spinUp)
                .whenBecomesFalse(Loader.INSTANCE.cutPower);

        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(Loader.INSTANCE.feedOut)
                .whenBecomesTrue(Intake.INSTANCE.pushOut)
                .whenBecomesFalse(Loader.INSTANCE.cutPower)
                .whenBecomesFalse(Intake.INSTANCE.cutPower);
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();
    }
}