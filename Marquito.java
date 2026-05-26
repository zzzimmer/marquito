package marquito;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Marquito extends AdvancedRobot {
	static final double WALL_MARGIN = 60;
	static final double WALL_STICK = 140;
	static final double ENEMY_FORCE = 55000;
	static final double WALL_FORCE = 9000;
	static final double CENTER_FORCE = 0.08;

	Rectangle2D.Double field;
	Map<String, Enemy> enemies = new HashMap<>();
	String targetName = null;
	int moveDir = 1;
	long lastDirChange = 0;

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);

		field = new Rectangle2D.Double(
				WALL_MARGIN,
				WALL_MARGIN,
				getBattleFieldWidth() - WALL_MARGIN * 2,
				getBattleFieldHeight() - WALL_MARGIN * 2
		);

		while (true) {
			chooseTarget();
			doMovement();
			doRadar();
			doGun();
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = getHeadingRadians() + e.getBearingRadians();
		Enemy en = enemies.get(e.getName());
		if (en == null) en = new Enemy();

		en.name = e.getName();
		en.energy = e.getEnergy();
		en.distance = e.getDistance();
		en.bearing = e.getBearingRadians();
		en.absBearing = absBearing;
		en.heading = e.getHeadingRadians();
		en.velocity = e.getVelocity();
		en.x = getX() + Math.sin(absBearing) * e.getDistance();
		en.y = getY() + Math.cos(absBearing) * e.getDistance();
		en.lastSeen = getTime();

		enemies.put(en.name, en);

		if (targetName == null || shouldReplaceTarget(enemies.get(targetName), en)) targetName = en.name;

		if (Math.random() < 0.025 && getTime() - lastDirChange > 18) {
			moveDir = -moveDir;
			lastDirChange = getTime();
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		enemies.remove(e.getName());
		if (e.getName().equals(targetName)) targetName = null;
	}

	public void onHitWall(HitWallEvent e) {
		moveDir = -moveDir;
		lastDirChange = getTime();
		setBack(60);
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.getName().equals(targetName) && e.getEnergy() < 8) setAhead(40);
		else {
			moveDir = -moveDir;
			setBack(50);
		}
	}

	void chooseTarget() {
		Enemy best = null;
		double bestScore = Double.POSITIVE_INFINITY;

		for (Enemy e : enemies.values()) {
			long age = getTime() - e.lastSeen;
			if (age > 20) continue;

			double score = e.distance * 1.2 + e.energy * 10 + Math.abs(e.velocity) * 35 + age * 20;

			if (best == null || score < bestScore) {
				best = e;
				bestScore = score;
			}
		}

		targetName = best == null ? null : best.name;
	}

	boolean shouldReplaceTarget(Enemy current, Enemy candidate) {
		if (candidate == null) return false;
		if (current == null) return true;
		if (getTime() - current.lastSeen > 20) return true;

		double currentScore =
				current.distance * 1.2 +
						current.energy * 10 +
						Math.abs(current.velocity) * 35;

		double candidateScore =
				candidate.distance * 1.2 +
						candidate.energy * 10 +
						Math.abs(candidate.velocity) * 35;

		return candidateScore < currentScore - 60;
	}

	void doRadar() {
		Enemy t = enemies.get(targetName);

		if (t != null && getTime() - t.lastSeen <= 4) {
			double turn = Utils.normalRelativeAngle(t.absBearing - getRadarHeadingRadians());
			setTurnRadarRightRadians(turn * 2.0);
		} else {
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
	}

	void doGun() {
		Enemy t = enemies.get(targetName);
		if (t == null) return;
		if (getTime() - t.lastSeen > 6) return;

		double power = firePower(t);
		double bulletSpeed = 20 - 3 * power;

		double tx = t.x;
		double ty = t.y;
		double vx = Math.sin(t.heading) * t.velocity;
		double vy = Math.cos(t.heading) * t.velocity;

		double time = Point2D.distance(getX(), getY(), tx, ty) / bulletSpeed;
		double futureX = tx + vx * time;
		double futureY = ty + vy * time;

		futureX = limit(WALL_MARGIN, futureX, getBattleFieldWidth() - WALL_MARGIN);
		futureY = limit(WALL_MARGIN, futureY, getBattleFieldHeight() - WALL_MARGIN);

		double aim = Math.atan2(futureX - getX(), futureY - getY());
		setTurnGunRightRadians(Utils.normalRelativeAngle(aim - getGunHeadingRadians()));

		if (getGunHeat() == 0 && Math.abs(getGunTurnRemainingRadians()) < Math.toRadians(12)) {
			setFire(power);
		}
	}

	double firePower(Enemy t) {
		double d = t.distance;
		double p = 1.5;

		if (getOthers() <= 2) p = 2.4;
		else if (getOthers() <= 5) p = 2.0;

		if (d < 180) p += 0.6;
		else if (d > 500) p -= 0.4;

		if (Math.abs(t.velocity) < 2) p += 0.4;
		if (t.energy < 10) p += 0.3;

		if (getEnergy() < 20) p = Math.min(p, 1.8);
		if (getEnergy() < 10) p = Math.min(p, 1.2);

		return limit(0.1, p, 3.0);
	}

	void doMovement() {
		double x = getX();
		double y = getY();

		double forceX = 0;
		double forceY = 0;

		for (Enemy e : enemies.values()) {
			if (getTime() - e.lastSeen > 20) continue;

			double dx = x - e.x;
			double dy = y - e.y;
			double dist2 = dx * dx + dy * dy;
			if (dist2 < 1) dist2 = 1;

			double weight = ENEMY_FORCE * (0.6 + e.energy / 100.0);
			forceX += dx / dist2 * weight;
			forceY += dy / dist2 * weight;
		}

		forceX += WALL_FORCE / Math.max(1, (x * x));
		forceX -= WALL_FORCE / Math.max(1, Math.pow(getBattleFieldWidth() - x, 2));
		forceY += WALL_FORCE / Math.max(1, (y * y));
		forceY -= WALL_FORCE / Math.max(1, Math.pow(getBattleFieldHeight() - y, 2));

		double cx = getBattleFieldWidth() / 2.0;
		double cy = getBattleFieldHeight() / 2.0;
		forceX += (cx - x) * CENTER_FORCE;
		forceY += (cy - y) * CENTER_FORCE;

		Enemy t = enemies.get(targetName);
		double angle = Math.atan2(forceX, forceY);

		if (t != null && getTime() - t.lastSeen <= 5) {
			double perp = t.absBearing + moveDir * Math.PI / 2.0;
			angle = normalizeBearing(perp * 0.65 + angle * 0.35);
		}

		angle = wallSmoothing(new Point2D.Double(x, y), angle, moveDir);
		setBackAsFront(angle);
	}

	double wallSmoothing(Point2D.Double pos, double angle, int orientation) {
		int safety = 0;
		while (!field.contains(project(pos, angle)) && safety++ < 100) {
			angle += orientation * 0.05;
		}
		return angle;
	}

	Point2D.Double project(Point2D.Double src, double angle) {
		return new Point2D.Double(src.x + Math.sin(angle) * Marquito.WALL_STICK, src.y + Math.cos(angle) * Marquito.WALL_STICK);
	}

	void setBackAsFront(double goAngle) {
		double angle = Utils.normalRelativeAngle(goAngle - getHeadingRadians());
		if (Math.abs(angle) > Math.PI / 2) {
			if (angle < 0) setTurnRightRadians(Math.PI + angle);
			else setTurnLeftRadians(Math.PI - angle);
			setBack(100);
		} else {
			if (angle < 0) setTurnLeftRadians(-angle);
			else setTurnRightRadians(angle);
			setAhead(100);
		}
	}

	double normalizeBearing(double angle) {
		return Utils.normalRelativeAngle(angle);
	}

	double limit(double min, double value, double max) {
		return Math.max(min, Math.min(max, value));
	}

	static class Enemy {
		String name;
		double energy;
		double distance;
		double bearing;
		double absBearing;
		double heading;
		double velocity;
		double x;
		double y;
		long lastSeen;
	}
}