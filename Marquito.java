package marquito;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Marquito extends AdvancedRobot {
	Map<String, Enemy> enemies = new HashMap<>();
	String targetName;
	int moveDir = 1;

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		while (true) {
			chooseTarget();
			if (nearWall()) {
				moveDir = -moveDir;
				setTurnRight(60);
			}
			setAhead(150 * moveDir);
			doRadar();
			doGun();
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		Enemy en = enemies.get(e.getName());
		if (en == null) en = new Enemy();

		double absBearing = getHeadingRadians() + e.getBearingRadians();

		en.name = e.getName();
		en.energy = e.getEnergy();
		en.distance = e.getDistance();
		en.absBearing = absBearing;
		en.heading = e.getHeadingRadians();
		en.velocity = e.getVelocity();
		en.x = getX() + Math.sin(absBearing) * e.getDistance();
		en.y = getY() + Math.cos(absBearing) * e.getDistance();
		en.lastSeen = getTime();

		enemies.put(en.name, en);
	}

	void chooseTarget() {
		Enemy best = null;
		double bestScore = Double.POSITIVE_INFINITY;

		for (Enemy e : enemies.values()) {
			if (getTime() - e.lastSeen > 20) continue;
			double score = e.distance + e.energy * 10;
			if (best == null || score < bestScore) {
				best = e;
				bestScore = score;
			}
		}

		targetName = best == null ? null : best.name;
	}

	void doRadar() {
		Enemy t = enemies.get(targetName);
		if (t == null) {
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			return;
		}
		double turn = Utils.normalRelativeAngle(t.absBearing - getRadarHeadingRadians());
		setTurnRadarRightRadians(turn * 2);
	}

	void doGun() {
		Enemy t = enemies.get(targetName);
		if (t == null) return;

		double power = Math.clamp(400 / t.distance, 1, 3);
		double bulletSpeed = 20 - 3 * power;
		double time = Point2D.distance(getX(), getY(), t.x, t.y) / bulletSpeed;
		double futureX = t.x + Math.sin(t.heading) * t.velocity * time;
		double futureY = t.y + Math.cos(t.heading) * t.velocity * time;
		double gunAngle = Math.atan2(futureX - getX(), futureY - getY());

		setTurnGunRightRadians(Utils.normalRelativeAngle(gunAngle - getGunHeadingRadians()));

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

	public void onRobotDeath(RobotDeathEvent e) {
		enemies.remove(e.getName());
		if (e.getName().equals(targetName)) targetName = null;
	}

	static class Enemy {
		String name;
		double energy;
		double distance;
		double absBearing;
		double heading;
		double velocity;
		double x;
		double y;
		long lastSeen;
	}
}