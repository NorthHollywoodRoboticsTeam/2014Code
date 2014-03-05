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
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {

    
    
    int Mecanum = 0,
            MecanumSplit = 1,
            AllInOne = 2;
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        /*try {
         if (((Double)(server.getValue("BLOB_COUNT"))).intValue() >= 2) {
         System.out.println("shoot NOW");
         }else{ 
         System.out.println("Don't shoot");
         }
         } catch (Exception ex) {
                
         }*/
    }

    /*
     Joystick spec.
     Driving: js1 Y for forward and backward, js1 X for turn and js2 X for sideways
     Winching: js3 up and down on knob   and js3 button 4 is auto winch up and js3 button 3 is autowinch down.
     Feeding: Toggle js2 10 for on and off and js2 11 overrides that and dose reverse
     Shooting: js3 button 2 turns it on and js3 trigger resets it
     */
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
    Joystick js1 = new Joystick(1), js2 = new Joystick(2), js3 = new Joystick(3);
    
    DigitalInput limitSwitch = new DigitalInput(1);

    //NetworkTable server = NetworkTable.getTable("SmartDashboard");
    //js 3 ax 5  is x on the knob and 6 is Y on the knob
    //The limit switch is inverted
    boolean button3State = false, button10State = false;

    int driveType = MecanumSplit;
    
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

            if (js3.getRawButton(3)) {
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = true;
                electroMagnet.set(Relay.Value.kForward);
                winchForward();
            }
            if (js3.getRawButton(4)) {
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
            if (js3.getRawAxis(6) != 0) {
                if (js3.getRawAxis(6) == -1) {
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
            if (js3.getRawButton(8)) {
                winchingStartTime = -1;
                isWinching = false;
                winchStop();
            }

            if (!js3.getRawButton(2)) {
                button3State = false;
            }
            if (js3.getRawButton(2) == true && button3State == false) {
                button3State = true;
                electroMagnet.set(Relay.Value.kForward);
            }

            if (js3.getTrigger()) {
                electroMagnet.set(Relay.Value.kOff);
            }
            if (js2.getRawButton(10) != button10State) {
                if (js2.getRawButton(10)) {
                    if (!feederOn) {
                        feederOn = true;
                    } else {
                        feederOn = false;
                    }
                    button10State = true;
                } else {
                    button10State = false;
                }
            }
            if (js3.getRawButton(11)) {
                feeder1.set(Relay.Value.kReverse);
                feeder2.set(Relay.Value.kReverse);
            } else {
                if (js3.getRawAxis(4) < .5) {

                    feeder1.set(Relay.Value.kForward);
                    feeder2.set(Relay.Value.kForward);
                } else {

                    feeder1.set(Relay.Value.kOff);
                    feeder2.set(Relay.Value.kOff);
                }
            }
            
            if (driveType == MecanumSplit) {
            drive.mecanumDrive_Cartesian(-js2.getY(), -js1.getX(), js2.getX() / 2, 0);
            } else if (driveType == Mecanum) {
                
            drive.mecanumDrive_Cartesian(-js2.getY(), -js2.getX(), js1.getX() / 2, 0);
            } else if (driveType == AllInOne) {
                
            drive.mecanumDrive_Cartesian(-js3.getY(), -js3.getX(), js3.getTwist()/ 2, 0);
            }

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
