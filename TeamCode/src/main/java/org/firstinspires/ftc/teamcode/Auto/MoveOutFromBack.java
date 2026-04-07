package org.firstinspires.ftc.teamcode.Auto;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Simple Straight Auto", preselectTeleOp = "DriveOpMode")
public class MoveOutFromBack extends NextFTCOpMode {

    public MoveOutFromBack() {
        addComponents(
                new SubsystemComponent(),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    public static class Paths {
        public PathChain forwardPath;

        public Paths(Follower follower) {
            forwardPath = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(57.0, 8.0),
                                    new Pose(57.0, 38.0)
                            )
                    )
                    .setLinearHeadingInterpolation(
                            Math.toRadians(90),
                            Math.toRadians(90)
                    )
                    .build();
        }
    }

    private Paths paths;

    @Override
    public void onInit() {
        follower().setStartingPose(new Pose(57.0, 8.0, Math.toRadians(90)));
        paths = new Paths(follower());

        telemetry.addLine("Ready");
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        new SequentialGroup(
                new FollowPath(paths.forwardPath)
        ).schedule();
    }

    @Override
    public void onUpdate() {
        telemetry.update();
    }
}

