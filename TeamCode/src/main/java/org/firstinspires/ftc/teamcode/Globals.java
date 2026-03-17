package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class Globals {
    public static Pose pose = new Pose(0,0,Math.toRadians(90));

    public enum Alliance {RED, BLUE}

    public static Alliance alliance = Alliance.RED;
}
