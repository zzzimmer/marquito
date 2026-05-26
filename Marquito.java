package marquito;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class Marquito extends AdvancedRobot {

	int moveDir = 1;

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		while (true) {
			if (nearWall()) {
				moveDir = -moveDir;
				setTurnRight(60);
			}
			setAhead(150 * moveDir);
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = getHeadingRadians() + e.getBearingRadians();
		double radarTurn = Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians());
		double power = Math.clamp(400 / e.getDistance(), 1, 3);

		setTurnRadarRightRadians(radarTurn * 2);
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - getGunHeadingRadians()));

		if (getGunHeat() == 0) setFire(power);
	}

	boolean nearWall() {
		return getX() < 80 || getY() < 80 || getX() > getBattleFieldWidth() - 80 || getY() > getBattleFieldHeight() - 80;
	}

	public void onHitWall(HitWallEvent e) {
		moveDir = -moveDir;
	}

	public void onHitByBullet(HitByBulletEvent e) {
		moveDir = -moveDir;
	}
}