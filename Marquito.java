package marquito;
import robocode.*;
import java.awt.Color;
import robocode.WinEvent;
import robocode.AdvancedRobot;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Marquito - a robot by (your name here)
 */
public class Marquito extends AdvancedRobot {
	boolean movingForward;
	/**
	 * run: Marquito's default behavior
	 */
	public void run() {
			setColors(Color.red,Color.blue,Color.green); // body,gun,radar??
		

		// Robot main loop
		while(true) {
			setAhead(40000);
			movingForward = true;
			setTurnGunRight(90);
			setTurnRight(90);
			waitFor(new TurnCompleteCondition(this));
			setTurnLeft(180);
			waitFor(new TurnCompleteCondition(this));
			setTurnGunLeft(90);
			setTurnRight(180);
			waitFor(new TurnCompleteCondition(this));
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		fire(1);
	}


	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}	

	public void onHitRobot(HitRobotEvent e) {
				reverseDirection();
	}
	
	public void reverseDirection() {
	if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}
}