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


@Autonomous(name = "Blue Goalside - 12", preselectTeleOp = "DriveOpMode")
public class BlueShortAuto12ball extends NextFTCOpMode {


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public BlueShortAuto12ball() {
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
        public PathChain Path9;
        public PathChain Path10;
        public PathChain Path11;

        public PathChain Path12;


        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(26.000, 128.000),
                                    new Pose(43.000, 94.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(125))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(43.000, 94.000),
                                    new Pose(48.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(125), Math.toRadians(180))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 84.000),
                                    new Pose(14.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(14.000, 84.000),
                                    new Pose(45.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(125))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(45.000, 92.000),
                                    new Pose(48.000, 59.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(125), Math.toRadians(180))
                    .build();

            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 61.000),
                                    new Pose(4.000, 61.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(4.000, 61.000),
                                    new Pose(20.000, 61.000)

                    )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(125))
                    .build();

            Path8 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(20.000, 61.000),
                                    new Pose(45.000, 92.000)

                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(125))
                    .build();

            Path9 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(45.000, 92.000),
                                    new Pose(48.000, 35.500)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(125), Math.toRadians(180))
                    .build();

            Path10 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 35.500),
                                    new Pose(2.000, 35.500)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path11 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(2.000, 35.500),
                                    new Pose(45.000, 92.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(125))
                    .build();
            Path12 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(48.000, 92.000),
                                    new Pose(4.000, 61.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

        }
    }

    private Paths paths;


    @Override
    public void onInit() {
        follower().setStartingPose(new Pose(24.5, 128, Math.toRadians(135)));
        paths = new Paths(follower());

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

                new Delay(0.5),
                //Drive to shoot
                new FollowPath(paths.Path1),
                Intake.INSTANCE.spinUp,
                //shoot
                Loader.INSTANCE.spinUp,
                new Delay(1.5), //shoot complete
                Loader.INSTANCE.cutPower,

                //drive to intake
                new FollowPath(paths.Path2),
                new FollowPath(paths.Path3),


                //drive to shoot, do so
                new FollowPath(paths.Path4),
                Loader.INSTANCE.spinUp,
                new Delay(1.5),
                Loader.INSTANCE.cutPower,

                //drive to intake
                new FollowPath(paths.Path5),
                new FollowPath(paths.Path6),


                //drive to shoot, do so
                new FollowPath(paths.Path7),


                //drive to shoot, do so
                new FollowPath(paths.Path8),
                Loader.INSTANCE.spinUp,
                new Delay(1.5),
                Loader.INSTANCE.cutPower,


                //drive to shoot, do so
                new FollowPath(paths.Path9),
                new FollowPath(paths.Path10),


                new FollowPath(paths.Path11),
                Loader.INSTANCE.spinUp,
                new Delay(1.5),
                Loader.INSTANCE.cutPower,

                new FollowPath(paths.Path12),




                Intake.INSTANCE.cutPower,
                Shooter.INSTANCE.cutPower
        ).schedule();
    }

    @Override
    public void onUpdate() {

        telemetry.addData("X", follower().getPose().getX());
        telemetry.addData("Y", follower().getPose().getY());
        telemetry.addData("H", follower().getPose().getHeading());
        panelsTelemetry.update(telemetry);
        telemetry.update();
    }
}