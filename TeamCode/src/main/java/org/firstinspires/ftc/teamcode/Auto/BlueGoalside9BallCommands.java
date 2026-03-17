package org.firstinspires.ftc.teamcode.Auto;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Camera;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterControlled;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Autonomous(name = "Blue Goalside - 9 (Command Based)")
public class BlueGoalside9BallCommands extends NextFTCOpMode {


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public BlueGoalside9BallCommands() {
        addComponents(
                new SubsystemComponent(ShooterControlled.INSTANCE, Feeder.INSTANCE, Intake.INSTANCE, Camera.INSTANCE),
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

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(24.500, 128.000),
                                    new Pose(50.000, 100.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(135))
                    .build();

            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(50.000, 100.000),
                                    new Pose(40.000, 84.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                    .build();

            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(40.000, 84.000),
                                    new Pose(20.000, 84.000)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(20.000, 84.000),
                                    new Pose(50.000, 100.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .build();

            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(50.000, 100.000),
                                    new Pose(40.000, 60.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                    .build();

            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(40.000, 60.000),
                                    new Pose(20.000, 60.000)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(20.000, 60.000),
                                    new Pose(50.000, 100.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .build();
        }
    }

    private Paths paths = new Paths(follower());


    @Override
    public void onInit() {
        Globals.alliance = Globals.Alliance.BLUE;
        follower().setStartingPose(new Pose(24.5, 128, Math.toRadians(135)));
    }


    @Override
    public void onStartButtonPressed() {
        new SequentialGroup(
                //Drive to shoot
                new ParallelGroup(
                        new FollowPath(paths.Path1),
                        ShooterControlled.INSTANCE.spinUp
                ),
                //shoot
                new ParallelGroup(
                        Intake.INSTANCE.spinUp,
                        Feeder.INSTANCE.spinUp
                ),
                new Delay(4), //shoot complete

                //drive to intake
                new FollowPath(paths.Path2),
                new ParallelGroup(
                        //drive into balls
                        new FollowPath(paths.Path3),
                        new SequentialGroup(
                                new ParallelGroup(
                                        new LambdaCommand().setIsDone(() -> ShooterControlled.INSTANCE.ballDetected.get()),
                                        new Delay(5) //deadline
                                ),
                                Feeder.INSTANCE.cutPower
                        )
                ),

                //drive to shoot, do so
                new FollowPath(paths.Path4),
                new ParallelGroup(
                        Feeder.INSTANCE.spinUp,
                        new Delay(4)
                ),

                //drive to intake
                new FollowPath(paths.Path5),

                new ParallelGroup(
                //drive into balls
                    new FollowPath(paths.Path6),
                    new SequentialGroup(
                            new ParallelGroup(
                                    new LambdaCommand().setIsDone(() -> ShooterControlled.INSTANCE.ballDetected.get()),
                                    new Delay(5) //deadline
                            ),
                            Feeder.INSTANCE.cutPower
                    )
                ),

                        //drive to shoot, do so
                        new FollowPath(paths.Path7),
                        new ParallelGroup(
                                Feeder.INSTANCE.spinUp,
                                new Delay(4)
                        )



        ).schedule();
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
        telemetry.update();

        Globals.pose = follower().getPose();
    }
}