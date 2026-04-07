package org.firstinspires.ftc.teamcode.Auto;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterControlled;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Disabled
@Autonomous(name = "Blue Far - Drive", preselectTeleOp = "DriveOpMode")
public class BlueFarDriveCommands extends NextFTCOpMode {


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public BlueFarDriveCommands() {
        addComponents(
                new SubsystemComponent(ShooterControlled.INSTANCE, Feeder.INSTANCE, Intake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    public static class Paths {
        public PathChain Path1;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56.000, 8.000),
                                    new Pose(62.000, 36.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                    .build();
        }
    }

    private Paths paths;


    @Override
    public void onInit() {
        follower().setStartingPose(new Pose(56, 8, Math.toRadians(90)));
        paths = new Paths(follower());

        panelsTelemetry.addLine("Ready");
        panelsTelemetry.update();
    }


    @Override
    public void onStartButtonPressed() {
        new FollowPath(paths.Path1).schedule();
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();
    }
}