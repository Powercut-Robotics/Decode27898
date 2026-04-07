package org.firstinspires.ftc.teamcode.Auto;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterControlled;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Autonomous(name = "Blue Goalside - 9", preselectTeleOp = "DriveOpMode")
public class BlueGoalside9BallCommands extends NextFTCOpMode {


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public BlueGoalside9BallCommands() {
        addComponents(
                new SubsystemComponent(ShooterControlled.INSTANCE, Feeder.INSTANCE, Intake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;
        public PathChain Path7;
        public PathChain Path8;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(26.000, 128.000),
                                    new Pose(43.000, 94.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(130))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(43.000, 94.000),
                                    new Pose(48.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(130), Math.toRadians(180))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 84.000),
                                    new Pose(16.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(16.000, 84.000),
                                    new Pose(45.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(130))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(45.000, 92.000),
                                    new Pose(48.000, 60.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(130), Math.toRadians(180))
                    .build();

            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 60.000),
                                    new Pose(13.500, 58.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(13.500, 58.000),
                                    new Pose(45.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(130))
                    .build();

            Path8 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(45.000, 92.000),
                                    new Pose(59.000, 120.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(130), Math.toRadians(180))
                    .build();
        }
    }

    private Paths paths;


    @Override
    public void onInit() {
        follower().setStartingPose(new Pose(24.5, 128, Math.toRadians(135)));
        paths = new Paths(follower());

        Intake.INSTANCE.initialize();
        Feeder.INSTANCE.initialize();

        panelsTelemetry.addLine("Ready");
        panelsTelemetry.update();
        telemetry.addLine("Ready");
        telemetry.update();
    }


    @Override
    public void onStartButtonPressed() {
        Intake.INSTANCE.spinUp.schedule();
        new SequentialGroup(
                Intake.INSTANCE.spinUp,
                ShooterControlled.INSTANCE.spinUp,

                new Delay(1),
                //Drive to shoot
                new FollowPath(paths.Path1),
                Intake.INSTANCE.spinUp,
                //shoot
                Feeder.INSTANCE.spinUp,
                new Delay(3.5), //shoot complete

                //drive to intake
                new FollowPath(paths.Path2),
                new FollowPath(paths.Path3),
                Feeder.INSTANCE.cutPower,

                //drive to shoot, do so
                new FollowPath(paths.Path4),
                Feeder.INSTANCE.spinUp,
                new Delay(3.5),

                //drive to intake
                new FollowPath(paths.Path5),
                new FollowPath(paths.Path6),
                Feeder.INSTANCE.cutPower,


                //drive to shoot, do so
                new FollowPath(paths.Path7),
                Feeder.INSTANCE.spinUp,
                new Delay(3.5),
                new FollowPath(paths.Path8),
                Intake.INSTANCE.cutPower,
                Feeder.INSTANCE.cutPower,
                ShooterControlled.INSTANCE.cutPower
        ).schedule();
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();;
    }
}