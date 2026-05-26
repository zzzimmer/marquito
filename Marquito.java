package marquito;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class Marquito extends AdvancedRobot {

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		while (true) {
			setAhead(120);
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = getHeadingRadians() + e.getBearingRadians();
		double radarTurn = Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians());
		setTurnRadarRightRadians(radarTurn * 2);
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - getGunHeadingRadians()));
		setFire(2);
	}

	public void onHitWall(HitWallEvent e) {
		setBack(80);
		setTurnRight(90);
	}
}