/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {

    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    
    SmartDashboard network = new SmartDashboard();
    public void autonomous() {
        drive.mecanumDrive_Cartesian(.5, 0, 0, 0);
        Timer.delay(1);
        drive.mecanumDrive_Cartesian(0, 0, 0, 0);
        
    }

    
    /*
     Port description:
     Electro magnet: Relay port on sidecar 1
     Feeder: Relay Port 2 and 3 (Reverse as needed)
     Winch: PWM Port 5 and 6
     Drive: PWM ports 1-4 AS LABELED ON MOTORS and with the Jags the right way.
     */
    RobotDrive drive = new RobotDrive(new Jaguar(2), new Jaguar(1), new InvertedSpeedController(new Jaguar(3)), new InvertedSpeedController(new Jaguar(4)));

    
    SpeedController buren1 = new Victor(5);
    SpeedController buren2 = new Jaguar(6);
    Relay feeder1 = new Relay(2), feeder2 = new Relay(3);
    Relay electroMagnet = new Relay(1);
    
    DigitalInput limitSwitch = new DigitalInput(1);

    boolean electroMagnetOnButtonState = false;

    JoystickLayout joystickLayout = new SplitMechanum(new Joystick(1), new Joystick(2), new Joystick(3));
    
    boolean feederOn = false;

    public final long winchingTimeUp = (long) (3.5 * 1000);
    public final long winchingTimeDown = (long) (3.56 * 1000);

    private final long winchLimitSwitchOverload = (long) (8);
    
    /**
     *
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long winchingStartTime = -1;

        boolean isWinching = false;

        //true = winching down false = winching up
        boolean autoWinchDirection = false;
        System.out.println("starting en.");
        while (isOperatorControl() && isEnabled()) {
            SmartDashboard.putBoolean("Limit Switch Status", !limitSwitch.get());

            if (joystickLayout.autoWinchDown()) {
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = true;
                electroMagnet.set(Relay.Value.kForward);
                winchForward();
            }
            if (joystickLayout.autoWinchUp()) {
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = false;
                winchReverse();
            }

            if (winchingStartTime != -1 && isWinching) {
                if (((autoWinchDirection == false) && System.currentTimeMillis() - winchingStartTime > winchingTimeUp) || 
                        ((autoWinchDirection == true) && System.currentTimeMillis() - winchingStartTime > winchingTimeDown)) {
                    winchingStartTime = -1;
                    isWinching = false;
                    winchStop();
                }
            }
            if (joystickLayout.manualWinchDown() || joystickLayout.manualWinchUp()) {
                if (joystickLayout.manualWinchDown()) {
                    winchForward();
                } else {
                    winchReverse();
                }
                winchingStartTime = -1;
                isWinching = false;
            } else if (!isWinching) {
                winchStop();
            }

            if (!limitSwitch.get() && isWinching) {
                if (autoWinchDirection == true) {
                    winchingStartTime = 
                            (System.currentTimeMillis() + winchLimitSwitchOverload)  //The system time we want the winch to stop at.
                            - winchingTimeDown;  //Minos the time it should winch down = the winch will stop in winchLimitSwitchOverload.
                }
            }
            if (joystickLayout.winchOff()) {
                winchingStartTime = -1;
                isWinching = false;
                winchStop();
            }

            if (!joystickLayout.electroMagnetOn()) {
                electroMagnetOnButtonState = false;
            }
            if (joystickLayout.electroMagnetOn() == true && electroMagnetOnButtonState == false) {
                electroMagnetOnButtonState = true;
                electroMagnet.set(Relay.Value.kForward);
            }

            if (joystickLayout.electroMagnetReset()) {
                electroMagnet.set(Relay.Value.kOff);
            }
            
            if (joystickLayout.overrideFeederReverse()) {
                feeder1.set(Relay.Value.kReverse);
                feeder2.set(Relay.Value.kReverse);
            } else {
                if (joystickLayout.setFeederOn()) {

                    feeder1.set(Relay.Value.kForward);
                    feeder2.set(Relay.Value.kForward);
                } else {

                    feeder1.set(Relay.Value.kOff);
                    feeder2.set(Relay.Value.kOff);
                }
            }
            
            drive.mecanumDrive_Cartesian(joystickLayout.driveForward(), joystickLayout.driveLeft(), joystickLayout.driveRotation(), 0);

        }
    }

    private void winchStop() {
        buren1.set(0);
        buren2.set(0);
    }

    private void winchForward() {
        buren1.set(1);
        buren2.set(-1 * .985);
    }

    private void winchReverse() {

        buren1.set(-1);
        buren2.set(1 * .985);
    }
    /**
     * This function is called once each time the robot enters test mode.
     */
}
