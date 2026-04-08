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
import org.firstinspires.ftc.teamcode.subsystems.Loader;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Autonomous(name = "Red Goalside - 9", preselectTeleOp = "DriveOpMode")
public class RedGoalside9BallCommands extends NextFTCOpMode {


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public RedGoalside9BallCommands() {
        addComponents(
                new SubsystemComponent(Shooter.INSTANCE, Loader.INSTANCE, Intake.INSTANCE),
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
                                    new Pose(118.000, 128.000),
                                    new Pose(102.000, 94.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(55))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(102.000, 94.000),
                                    new Pose(96.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(55), Math.toRadians(0))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(96.000, 84.000),
                                    new Pose(128.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(128.000, 84.000),
                                    new Pose(100.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(55))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(100.000, 92.000),
                                    new Pose(96.000, 59.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(55), Math.toRadians(0))
                    .build();

            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(96.000, 59.000),
                                    new Pose(129.000, 59.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                    .build();

            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(130.000, 59.000),
                                    new Pose(100.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(55))
                    .build();

            Path8 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(100.000, 92.000),
                                    new Pose(86.000, 120.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(55), Math.toRadians(0))
                    .build();
        }
    }

    private Paths paths;


    @Override
    public void onInit() {
        paths = new Paths(follower());
        follower().setStartingPose(new Pose(119.5, 128, Math.toRadians(45)));

        Intake.INSTANCE.initialize();
        Loader.INSTANCE.initialize();

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
                Shooter.INSTANCE.spinUpMid,

                new Delay(1),
                //Drive to shoot
                new FollowPath(paths.Path1),
                Intake.INSTANCE.spinUp,
                //shoot
                Loader.INSTANCE.spinUp,
                new Delay(3.5), //shoot complete

                //drive to intake
                new FollowPath(paths.Path2),
                new FollowPath(paths.Path3),
                Loader.INSTANCE.cutPower,

                //drive to shoot, do so
                new FollowPath(paths.Path4),
                Loader.INSTANCE.spinUp,
                new Delay(3.5),

                //drive to intake
                new FollowPath(paths.Path5),
                new FollowPath(paths.Path6),
                Loader.INSTANCE.cutPower,


                //drive to shoot, do so
                new FollowPath(paths.Path7),
                Loader.INSTANCE.spinUp,
                new Delay(3.5),
                new FollowPath(paths.Path8),
                Intake.INSTANCE.cutPower,
                Loader.INSTANCE.cutPower,
                Shooter.INSTANCE.cutPower
        ).schedule();
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();
    }
}