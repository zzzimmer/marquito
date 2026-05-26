package marquito;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.Point2D;

public class Marquito extends AdvancedRobot {
	String target;
	double targetDistance = Double.POSITIVE_INFINITY;
	int moveDir = 1;

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		while (true) {
			target = null;
			targetDistance = Double.POSITIVE_INFINITY;
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			if (nearWall()) {
				moveDir = -moveDir;
				setTurnRight(60);
			}
			setAhead(150 * moveDir);
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (target == null || e.getDistance() < targetDistance || e.getName().equals(target)) {
			target = e.getName();
			targetDistance = e.getDistance();

			double absBearing = getHeadingRadians() + e.getBearingRadians();
			double power = Math.clamp(400 / e.getDistance(), 1, 3);
			double bulletSpeed = 20 - 3 * power;

			double enemyX = getX() + Math.sin(absBearing) * e.getDistance();
			double enemyY = getY() + Math.cos(absBearing) * e.getDistance();
			double time = Point2D.distance(getX(), getY(), enemyX, enemyY) / bulletSpeed;
			double futureX = enemyX + Math.sin(e.getHeadingRadians()) * e.getVelocity() * time;
			double futureY = enemyY + Math.cos(e.getHeadingRadians()) * e.getVelocity() * time;
			double gunAngle = Math.atan2(futureX - getX(), futureY - getY());

			double radarTurn = Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians());

			setTurnRadarRightRadians(radarTurn * 2);
			setTurnGunRightRadians(Utils.normalRelativeAngle(gunAngle - getGunHeadingRadians()));

			if (getGunHeat() == 0) {
				setFire(power);
			}
		}
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