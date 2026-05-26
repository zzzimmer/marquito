package marquito;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class Marquito extends AdvancedRobot {

	int moveDirection = 1;

	public void run() {
		setColors(Color.red, Color.blue, Color.green);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		
		while(true) {
			if (getRadarTurnRemaining() == 0) {
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			}
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = getHeadingRadians() + e.getBearingRadians();
		
		double radarTurn = Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians());
		setTurnRadarRightRadians(radarTurn * 2.0);
		
		setTurnRightRadians(e.getBearingRadians() + Math.PI/2 - 0.5 * moveDirection);
		setAhead(100 * moveDirection);

		double bulletPower = Math.min(3.0, getEnergy());
		double bulletSpeed = 20 - 3 * bulletPower;
		long time = (long)(e.getDistance() / bulletSpeed);
		
		double futureX = getX() + Math.sin(absBearing) * e.getDistance() + Math.sin(e.getHeadingRadians()) * e.getVelocity() * time;
		double futureY = getY() + Math.cos(absBearing) * e.getDistance() + Math.cos(e.getHeadingRadians()) * e.getVelocity() * time;
		
		double gunTurn = Utils.normalRelativeAngle(Math.atan2(futureX - getX(), futureY - getY()) - getGunHeadingRadians());
		setTurnGunRightRadians(gunTurn);
		
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
			setFire(bulletPower);
		}
	}

	public void onHitWall(HitWallEvent e) {
		moveDirection *= -1;
	}
	
	public void onHitRobot(HitRobotEvent e) {
		moveDirection *= -1;
	}
}